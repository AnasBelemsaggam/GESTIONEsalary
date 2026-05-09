IF DB_ID('FinanceTracker') IS NOT NULL
BEGIN
    ALTER DATABASE FinanceTracker SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE FinanceTracker;
END
GO

CREATE DATABASE FinanceTracker;
GO

USE FinanceTracker;
GO

CREATE TABLE Users (
                       userId INT IDENTITY(1,1) PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       passwordHash VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL DEFAULT 'USER',
                       createdAt DATETIME DEFAULT GETDATE()
);
GO

CREATE TABLE Categories (
                            categoryId INT IDENTITY(1,1) PRIMARY KEY,
                            name VARCHAR(50) NOT NULL,
                            icon VARCHAR(10) NULL
);
GO

INSERT INTO Categories (name, icon)
VALUES
('Rent', NULL),
('Groceries', NULL),
('Transport', NULL),
('Bills', NULL),
('Healthcare', NULL),
('Leisure', NULL),
('Other', NULL);
GO

CREATE TABLE Salaries (
                          salaryId INT IDENTITY(1,1) PRIMARY KEY,
                          userId INT NOT NULL,
                          amount DECIMAL(12,2) NOT NULL CHECK (amount >= 0),
                          month TINYINT NOT NULL CHECK (month BETWEEN 1 AND 12),
    year SMALLINT NOT NULL CHECK (year >= 2000),
    createdAt DATETIME DEFAULT GETDATE(),

    CONSTRAINT FK_Salaries_Users
    FOREIGN KEY (userId)
    REFERENCES Users(userId)
    ON DELETE CASCADE,

    CONSTRAINT UQ_Salary_UserMonthYear
    UNIQUE (userId, month, year)
);
GO

CREATE TABLE Expenses (
                          expenseId INT IDENTITY(1,1) PRIMARY KEY,
                          userId INT NOT NULL,
                          categoryId INT NOT NULL,
                          amount DECIMAL(12,2) NOT NULL CHECK (amount > 0),
                          description VARCHAR(200) NULL,
                          expenseDate DATE NOT NULL DEFAULT CAST(GETDATE() AS DATE),
                          createdAt DATETIME DEFAULT GETDATE(),

                          CONSTRAINT FK_Expenses_Users
                              FOREIGN KEY (userId)
                                  REFERENCES Users(userId)
                                  ON DELETE CASCADE,

                          CONSTRAINT FK_Expenses_Categories
                              FOREIGN KEY (categoryId)
                                  REFERENCES Categories(categoryId)
);
GO

CREATE INDEX IX_Expenses_UserDate
    ON Expenses(userId, expenseDate);
GO

CREATE TABLE MonthlyReports (
                                reportId INT IDENTITY(1,1) PRIMARY KEY,
                                userId INT NOT NULL,
                                month TINYINT NOT NULL CHECK (month BETWEEN 1 AND 12),
    year SMALLINT NOT NULL,
    salary DECIMAL(12,2) NOT NULL,
    totalExpenses DECIMAL(12,2) NOT NULL,
    remaining AS (salary - totalExpenses),
    generatedAt DATETIME DEFAULT GETDATE(),

    CONSTRAINT FK_Reports_Users
    FOREIGN KEY (userId)
    REFERENCES Users(userId)
    ON DELETE CASCADE,

    CONSTRAINT UQ_Report_UserMonthYear
    UNIQUE (userId, month, year)
);
GO

CREATE VIEW vw_MonthlySummary
AS
SELECT
    s.userId,
    u.username,
    s.month,
    s.year,
    s.amount AS salary,
    ISNULL(SUM(e.amount), 0) AS totalExpenses,
    s.amount - ISNULL(SUM(e.amount), 0) AS remaining
FROM Salaries s
         JOIN Users u ON s.userId = u.userId
         LEFT JOIN Expenses e
                   ON e.userId = s.userId
                       AND MONTH(e.expenseDate) = s.month
        AND YEAR(e.expenseDate) = s.year
        GROUP BY
        s.userId,
        u.username,
        s.month,
        s.year,
        s.amount;
GO

CREATE OR ALTER PROCEDURE sp_AddExpense
    @userId INT,
    @categoryId INT,
    @amount DECIMAL(12,2),
    @description VARCHAR(200) = NULL,
    @date DATE = NULL
    AS
BEGIN
INSERT INTO Expenses(userId, categoryId, amount, description, expenseDate)
VALUES(@userId, @categoryId, @amount, @description, ISNULL(@date, CAST(GETDATE() AS DATE)));
END;
GO

CREATE OR ALTER PROCEDURE sp_GetMonthlySummary
    @userId INT,
    @month TINYINT,
    @year SMALLINT
    AS
BEGIN
SELECT *
FROM vw_MonthlySummary
WHERE userId = @userId
          AND month = @month
          AND year = @year;
END;
GO

INSERT INTO Users (username, email, passwordHash, role)
VALUES
('anas', 'anas@email.com', '123456', 'ADMIN'),
('sara', 'sara@email.com', '123456', 'USER');
GO

IF NOT EXISTS (SELECT * FROM sys.server_principals WHERE name = 'finance_user')
BEGIN
    CREATE LOGIN finance_user WITH PASSWORD = 'Finance2026!Strong';
END
GO

USE FinanceTracker;
GO

IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = 'finance_user')
BEGIN
    CREATE USER finance_user FOR LOGIN finance_user;
END
GO

ALTER ROLE db_owner ADD MEMBER finance_user;
GO