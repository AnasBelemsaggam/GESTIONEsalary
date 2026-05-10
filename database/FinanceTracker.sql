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

-- =========================================
-- USERS TABLE
-- =========================================

CREATE TABLE Users (
                       userId INT IDENTITY(1,1) PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       passwordHash VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL DEFAULT 'USER',
                       createdAt DATETIME DEFAULT GETDATE()
);
GO

-- =========================================
-- CATEGORIES TABLE
-- =========================================

CREATE TABLE Categories (
                            categoryId INT IDENTITY(1,1) PRIMARY KEY,
                            name VARCHAR(50) NOT NULL,
                            icon VARCHAR(10) NULL
);
GO

INSERT INTO Categories(name, icon)
VALUES
('Rent', NULL),
('Groceries', NULL),
('Transport', NULL),
('Bills', NULL),
('Healthcare', NULL),
('Leisure', NULL),
('Other', NULL);
GO

-- =========================================
-- SALARIES TABLE
-- =========================================

CREATE TABLE Salaries (
                          salaryId INT IDENTITY(1,1) PRIMARY KEY,
                          userId INT NOT NULL,
                          amount DECIMAL(12,2) NOT NULL CHECK (amount >= 0),
                          month INT NOT NULL CHECK (month BETWEEN 1 AND 12),
    year INT NOT NULL CHECK (year >= 2000),
    createdAt DATETIME DEFAULT GETDATE(),

    CONSTRAINT FK_Salaries_Users
    FOREIGN KEY (userId)
    REFERENCES Users(userId)
    ON DELETE CASCADE
);
GO

-- =========================================
-- EXPENSES TABLE
-- =========================================

CREATE TABLE Expenses (
                          expenseId INT IDENTITY(1,1) PRIMARY KEY,
                          userId INT NOT NULL,
                          categoryId INT NOT NULL,
                          amount DECIMAL(12,2) NOT NULL CHECK (amount > 0),
                          description VARCHAR(255),
                          expenseDate DATE NOT NULL DEFAULT GETDATE(),
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

-- =========================================
-- MONTHLY REPORTS TABLE
-- =========================================

CREATE TABLE MonthlyReports (
                                reportId INT IDENTITY(1,1) PRIMARY KEY,
                                userId INT NOT NULL,
                                month INT NOT NULL,
                                year INT NOT NULL,
                                salary DECIMAL(12,2) NOT NULL,
                                totalExpenses DECIMAL(12,2) NOT NULL,
                                remaining AS (salary - totalExpenses),
                                generatedAt DATETIME DEFAULT GETDATE(),

                                CONSTRAINT FK_Reports_Users
                                    FOREIGN KEY (userId)
                                        REFERENCES Users(userId)
                                        ON DELETE CASCADE
);
GO

-- =========================================
-- VIEW: MONTHLY SUMMARY
-- =========================================

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

         JOIN Users u
              ON s.userId = u.userId

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

-- =========================================
-- VIEW: CATEGORY STATISTICS
-- =========================================

CREATE VIEW vw_MonthlyByCategory
AS
SELECT
    c.name AS category,
    SUM(e.amount) AS totalAmount
FROM Expenses e
         JOIN Categories c
              ON e.categoryId = c.categoryId
GROUP BY c.name;
GO

-- =========================================
-- PROCEDURE: ADD EXPENSE
-- =========================================

CREATE PROCEDURE sp_AddExpense
    @userId INT,
    @categoryId INT,
    @amount DECIMAL(12,2),
    @description VARCHAR(255),
    @expenseDate DATE
AS
BEGIN

INSERT INTO Expenses(
    userId,
    categoryId,
    amount,
    description,
    expenseDate
)
VALUES(
          @userId,
          @categoryId,
          @amount,
          @description,
          @expenseDate
      );

END;
GO

-- =========================================
-- PROCEDURE: GET MONTHLY SUMMARY
-- =========================================

CREATE PROCEDURE sp_GetMonthlySummary
    @userId INT,
    @month INT,
    @year INT
AS
BEGIN

SELECT *
FROM vw_MonthlySummary
WHERE userId = @userId
          AND month = @month
          AND year = @year;

END;
GO

-- =========================================
-- DEFAULT USERS
-- =========================================

INSERT INTO Users(username, email, passwordHash, role)
VALUES
('anas', 'anas@gmail.com', '123456', 'ADMIN'),
('sara', 'sara@gmail.com', '123456', 'USER');
GO

-- =========================================
-- SQL SERVER LOGIN
-- =========================================

IF NOT EXISTS (
    SELECT *
    FROM sys.server_principals
    WHERE name = 'finance_user'
)
BEGIN
    CREATE LOGIN finance_user
    WITH PASSWORD = 'Finance2026!Strong';
END
GO

USE FinanceTracker;
GO

IF NOT EXISTS (
    SELECT *
    FROM sys.database_principals
    WHERE name = 'finance_user'
)
BEGIN
    CREATE USER finance_user
    FOR LOGIN finance_user;
END
GO

ALTER ROLE db_owner
ADD MEMBER finance_user;
GOgories;