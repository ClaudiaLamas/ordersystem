package com.claudialamas.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class EmailValidationClient {

    private final RestTemplate rt;
    private final ObjectMapper mapper;
    private final String baseUrl;
    private final String path;
    private final String apiKey;
    private final boolean enabled;


    public EmailValidationClient(RestTemplate rt,
                                 ObjectMapper mapper,
                                 @Value("${external.email.base-url}") String baseUrl,
                                 @Value("${external.email.path}") String path,
                                 @Value("${external.email.api-key}") String apiKey,
                                 @Value("${external.email.enabled:true}") boolean enabled) {
        this.rt = rt;
        this.mapper = mapper;
        this.baseUrl = baseUrl;
        this.path = path;
        this.apiKey = apiKey;
        this.enabled = enabled;
    }

    public EmailValidationDto validate(String email) {

        if (!enabled) {
            EmailValidationDto r = new EmailValidationDto();
            r.setResult("skipped");
            r.setSuccess(true);
            r.setDetails("disabled");
            return r;
        }

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + path)
                .queryParam("apikey", apiKey)
                .queryParam("email", email)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.USER_AGENT, "ordersystem/1.0"); // alguns gateways exigem UA
        HttpEntity<Void> req = new HttpEntity<>(headers);

        try {
            // 1) pedir como String para não falhar por Content-Type errado
            ResponseEntity<String> resp = rt.exchange(url, HttpMethod.GET, req, String.class);
            String body = resp.getBody();
            if (body == null || body.trim().isEmpty()) {
                return error("EMPTY_BODY", "Empty response body");
            }

            // 2) se veio HTML, sinaliza claramente
            MediaType ct = resp.getHeaders().getContentType();
            if (ct != null && ct.toString().toLowerCase().contains("text/html")) {
                return error("HTML_RESPONSE", bodySnippet(body));
            }

            // 3) tenta parsear como JSON
            EmailValidationDto dto = mapper.readValue(body, EmailValidationDto.class);

            // Captain Verify costuma devolver "success": true/false
            // Se vier null, considera válido se result == "valid"
            if (dto.getSuccess() == null) {
                dto.setSuccess(dto.isValid());
            }
            return dto;

        } catch (HttpStatusCodeException e) {
            return error("HTTP_" + e.getStatusCode().value(), bodySnippet(e.getResponseBodyAsString()));
        } catch (Exception e) {
            return error("CALL_ERROR", e.getMessage());
        }
    }

    private String bodySnippet(String body) {
        if (body == null) return null;
        String t = body.replaceAll("\\s+"," ").trim();
        return t.length() <= 240 ? t : t.substring(0, 240) + "...";
    }

    private static EmailValidationDto error(String details, String msg) {
        EmailValidationDto r = new EmailValidationDto();
        r.setResult("error");
        r.setSuccess(false);
        r.setDetails(details);
        r.setMessage(msg);
        return r;
    }


}

