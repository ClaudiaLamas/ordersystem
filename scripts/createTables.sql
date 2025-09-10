/* Create tables (fully qualified, so no USE needed) */

/* clients */
IF OBJECT_ID(N'ordersdb.dbo.clients', N'U') IS NULL
BEGIN
    CREATE TABLE ordersdb.dbo.clients (
        id          bigint IDENTITY(1,1) NOT NULL,
        created_at  datetime2 NULL,
        email       varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
        name        varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
        vat_number  int NOT NULL,
        CONSTRAINT PK__clients__3213E83FC9F4ACB8 PRIMARY KEY (id),
        CONSTRAINT UK_8h4uabcj9fwdqet2p7of8qqsg UNIQUE (vat_number),
        CONSTRAINT UK_srv16ica2c1csub334bxjjb59 UNIQUE (email)
    );
END;

/* error_log */
IF OBJECT_ID(N'ordersdb.dbo.error_log', N'U') IS NULL
BEGIN
    CREATE TABLE ordersdb.dbo.error_log (
        id              bigint IDENTITY(1,1) NOT NULL,
        exception_type  varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
        [level]         varchar(32)  COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
        message         varchar(2000) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
        occurred_at     datetimeoffset(3) NOT NULL,
        CONSTRAINT PK__error_lo__3213E83FDE7E8846 PRIMARY KEY (id)
    );
END;

/* orders */
IF OBJECT_ID(N'ordersdb.dbo.orders', N'U') IS NULL
BEGIN
    CREATE TABLE ordersdb.dbo.orders (
        id                 bigint IDENTITY(1,1) NOT NULL,
        created_at         datetime2 NOT NULL,
        status             varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
        validation_result  nvarchar(MAX) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
        value              numeric(12,2) NOT NULL,
        client_id          bigint NOT NULL,
        CONSTRAINT PK__orders__3213E83FDC26513B PRIMARY KEY (id),
        CONSTRAINT FKm2dep9derpoaehshbkkatam3v
            FOREIGN KEY (client_id) REFERENCES ordersdb.dbo.clients(id)
    );
END;

/* order_status_history */
IF OBJECT_ID(N'ordersdb.dbo.order_status_history', N'U') IS NULL
BEGIN
    CREATE TABLE ordersdb.dbo.order_status_history (
        id          bigint IDENTITY(1,1) NOT NULL,
        changed_at  datetime2 NULL,
        changed_by  varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
        new_status  varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
        old_status  varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
        order_id    bigint NOT NULL,
        CONSTRAINT PK__order_st__3213E83F7E5E93F5 PRIMARY KEY (id),
        CONSTRAINT FKnmcbg3mmbt8wfva97ra40nmp3
            FOREIGN KEY (order_id) REFERENCES ordersdb.dbo.orders(id)
    );
END;
