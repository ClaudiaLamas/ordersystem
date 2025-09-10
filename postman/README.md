{
	"info": {
		"_postman_id": "b5915d44-3757-4717-9b0b-53effe9bee81",
		"name": "Egitron - BE Test",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
		"_exporter_id": "25165449"
	},
	"item": [
		{
			"name": "client",
			"item": [
				{
					"name": "List All Clients",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/api/v1/clients/",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"api",
								"v1",
								"clients",
								""
							]
						}
					},
					"response": []
				},
				{
					"name": "Add New Client",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\n    \"name\": \"CLAUDIA\",\n    \"email\": \"lamasclaudiadev@xpt.com\",\n    \"vatNumber\": 123987\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "{{baseUrl}}/api/v1/clients/",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"api",
								"v1",
								"clients",
								""
							]
						}
					},
					"response": []
				},
				{
					"name": "Find Client By Email",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/api/v1/clients/email?email=claudia.c.lamas@gmail.com",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"api",
								"v1",
								"clients",
								"email"
							],
							"query": [
								{
									"key": "email",
									"value": "claudia.c.lamas@gmail.com"
								}
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "order",
			"item": [
				{
					"name": "List All Orders",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/api/v1/orders/",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"api",
								"v1",
								"orders",
								""
							]
						}
					},
					"response": []
				},
				{
					"name": "Add New Order",
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Accept",
								"value": "application/json",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n    \"clientId\": 10002,\n    \"value\": 91111.35\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "{{baseUrl}}/api/v1/orders/",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"api",
								"v1",
								"orders",
								""
							]
						}
					},
					"response": []
				},
				{
					"name": "Update Order Status",
					"request": {
						"method": "PATCH",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\n    \"newOrderStatus\": \"APROVADO\"\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "{{baseUrl}}/api/v1/orders/3/status",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"api",
								"v1",
								"orders",
								"3",
								"status"
							]
						}
					},
					"response": []
				},
				{
					"name": "List Client Orders",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/api/v1/orders/1/client_orders",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"api",
								"v1",
								"orders",
								"1",
								"client_orders"
							]
						}
					},
					"response": []
				},
				{
					"name": "List Orders By Status",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/api/v1/orders/statusFilter?status=REJEITADO",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"api",
								"v1",
								"orders",
								"statusFilter"
							],
							"query": [
								{
									"key": "status",
									"value": "REJEITADO"
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "List by Range Date",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/api/v1/orders/dateFilter?startDate=2025-09-08&endDate=2025-09-08",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"api",
								"v1",
								"orders",
								"dateFilter"
							],
							"query": [
								{
									"key": "startDate",
									"value": "2025-09-08"
								},
								{
									"key": "endDate",
									"value": "2025-09-08"
								}
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Error Log",
			"item": [
				{
					"name": "Error Report by Email",
					"request": {
						"method": "POST",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/admin/error-report/today",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"admin",
								"error-report",
								"today"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Login",
			"event": [
				{
					"listen": "test",
					"script": {
						"exec": [
							"if (pm.response.code === 200) {",
							"  const json = pm.response.json();",
							"  const token = json.access_token;",
							"  if (token) {",
							"    pm.collectionVariables.set('jwtToken', token); ",
							"    console.log('Token saved:', token.substring(0, 20) + '...');",
							"  } else {",
							"    console.warn('I did not found token in response: ', json);",
							"  }",
							"}"
						],
						"type": "text/javascript",
						"packages": {}
					}
				}
			],
			"request": {
				"method": "POST",
				"header": [
					{
						"key": "Content-Type",
						"value": "application/json",
						"type": "text"
					}
				],
				"body": {
					"mode": "raw",
					"raw": "{\n    \"username\": \"demo\",\n    \"password\": \"demo\"\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "{{baseUrl}}/auth/login",
					"host": [
						"{{baseUrl}}"
					],
					"path": [
						"auth",
						"login"
					]
				}
			},
			"response": []
		}
	],
	"auth": {
		"type": "bearer",
		"bearer": [
			{
				"key": "token",
				"value": "{{jwtToken}}",
				"type": "string"
			}
		]
	},
	"event": [
		{
			"listen": "prerequest",
			"script": {
				"type": "text/javascript",
				"packages": {},
				"exec": [
					""
				]
			}
		},
		{
			"listen": "test",
			"script": {
				"type": "text/javascript",
				"packages": {},
				"exec": [
					""
				]
			}
		}
	],
	"variable": [
		{
			"key": "baseUrl",
			"value": "",
			"type": "string"
		},
		{
			"key": "jwtToken",
			"value": "",
			"type": "string"
		}
	]
}