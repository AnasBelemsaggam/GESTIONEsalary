USE [master]
GO
/****** Object:  Database [FinanceTracker]    Script Date: 09/05/2026 14:45:45 ******/
CREATE DATABASE [FinanceTracker]
 CONTAINMENT = NONE
 ON  PRIMARY
( NAME = N'FinanceTracker', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.SQLEXPRESS\MSSQL\DATA\FinanceTracker.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON
( NAME = N'FinanceTracker_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.SQLEXPRESS\MSSQL\DATA\FinanceTracker_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [FinanceTracker] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [FinanceTracker].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [FinanceTracker] SET ANSI_NULL_DEFAULT OFF
GO
ALTER DATABASE [FinanceTracker] SET ANSI_NULLS OFF
GO
ALTER DATABASE [FinanceTracker] SET ANSI_PADDING OFF
GO
ALTER DATABASE [FinanceTracker] SET ANSI_WARNINGS OFF
GO
ALTER DATABASE [FinanceTracker] SET ARITHABORT OFF
GO
ALTER DATABASE [FinanceTracker] SET AUTO_CLOSE ON
GO
ALTER DATABASE [FinanceTracker] SET AUTO_SHRINK OFF
GO
ALTER DATABASE [FinanceTracker] SET AUTO_UPDATE_STATISTICS ON
GO
ALTER DATABASE [FinanceTracker] SET CURSOR_CLOSE_ON_COMMIT OFF
GO
ALTER DATABASE [FinanceTracker] SET CURSOR_DEFAULT  GLOBAL
GO
ALTER DATABASE [FinanceTracker] SET CONCAT_NULL_YIELDS_NULL OFF
GO
ALTER DATABASE [FinanceTracker] SET NUMERIC_ROUNDABORT OFF
GO
ALTER DATABASE [FinanceTracker] SET QUOTED_IDENTIFIER OFF
GO
ALTER DATABASE [FinanceTracker] SET RECURSIVE_TRIGGERS OFF
GO
ALTER DATABASE [FinanceTracker] SET  ENABLE_BROKER
GO
ALTER DATABASE [FinanceTracker] SET AUTO_UPDATE_STATISTICS_ASYNC OFF
GO
ALTER DATABASE [FinanceTracker] SET DATE_CORRELATION_OPTIMIZATION OFF
GO
ALTER DATABASE [FinanceTracker] SET TRUSTWORTHY OFF
GO
ALTER DATABASE [FinanceTracker] SET ALLOW_SNAPSHOT_ISOLATION OFF
GO
ALTER DATABASE [FinanceTracker] SET PARAMETERIZATION SIMPLE
GO
ALTER DATABASE [FinanceTracker] SET READ_COMMITTED_SNAPSHOT OFF
GO
ALTER DATABASE [FinanceTracker] SET HONOR_BROKER_PRIORITY OFF
GO
ALTER DATABASE [FinanceTracker] SET RECOVERY SIMPLE
GO
ALTER DATABASE [FinanceTracker] SET  MULTI_USER
GO
ALTER DATABASE [FinanceTracker] SET PAGE_VERIFY CHECKSUM
GO
ALTER DATABASE [FinanceTracker] SET DB_CHAINING OFF
GO
ALTER DATABASE [FinanceTracker] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF )
GO
ALTER DATABASE [FinanceTracker] SET TARGET_RECOVERY_TIME = 60 SECONDS
GO
ALTER DATABASE [FinanceTracker] SET DELAYED_DURABILITY = DISABLED
GO
ALTER DATABASE [FinanceTracker] SET ACCELERATED_DATABASE_RECOVERY = OFF
GO
ALTER DATABASE [FinanceTracker] SET QUERY_STORE = ON
GO
ALTER DATABASE [FinanceTracker] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [FinanceTracker]
GO
/****** Object:  User [finance_user]    Script Date: 09/05/2026 14:45:46 ******/
CREATE USER [finance_user] FOR LOGIN [finance_user] WITH DEFAULT_SCHEMA=[dbo]
GO
ALTER ROLE [db_owner] ADD MEMBER [finance_user]
GO
/****** Object:  Table [dbo].[Users]    Script Date: 09/05/2026 14:45:46 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Users](
    [userId] [int] IDENTITY(1,1) NOT NULL,
    [username] [nvarchar](50) NOT NULL,
    [email] [nvarchar](100) NOT NULL,
    [passwordHash] [nvarchar](255) NOT NULL,
    [createdAt] [datetime] NULL,
    [role] [varchar](20) NOT NULL,
    PRIMARY KEY CLUSTERED
(
[userId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
    UNIQUE NONCLUSTERED
(
[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
    UNIQUE NONCLUSTERED
(
[username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[Categories]    Script Date: 09/05/2026 14:45:46 ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[Categories](
    [categoryId] [int] IDENTITY(1,1) NOT NULL,
    [name] [nvarchar](50) NOT NULL,
    [icon] [nvarchar](10) NULL,
    PRIMARY KEY CLUSTERED
(
[categoryId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[Expenses]    Script Date: 09/05/2026 14:45:46 ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[Expenses](
    [expenseId] [int] IDENTITY(1,1) NOT NULL,
    [userId] [int] NOT NULL,
    [categoryId] [int] NOT NULL,
    [amount] [decimal](12, 2) NOT NULL,
    [description] [nvarchar](200) NULL,
    [expenseDate] [date] NOT NULL,
    [createdAt] [datetime] NULL,
    PRIMARY KEY CLUSTERED
(
[expenseId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  View [dbo].[vw_MonthlyByCategory]    Script Date: 09/05/2026 14:45:46 ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO

-- ============================================================
-- 6. VIEWS
-- ============================================================

-- Monthly expense breakdown per category
CREATE VIEW [dbo].[vw_MonthlyByCategory]
AS
SELECT
    u.userId,
    u.username,
    c.name AS category,
    MONTH(e.expenseDate) AS [month],
    YEAR(e.expenseDate) AS [year],
    SUM(e.amount) AS totalAmount,
    COUNT(*) AS numTransactions
FROM Expenses e
    JOIN Users u
ON e.userId = u.userId
    JOIN Categories c
    ON e.categoryId = c.categoryId
GROUP BY
    u.userId,
    u.username,
    c.name,
    MONTH(e.expenseDate),
    YEAR(e.expenseDate);
GO
/****** Object:  Table [dbo].[Salaries]    Script Date: 09/05/2026 14:45:46 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Salaries](
    [salaryId] [int] IDENTITY(1,1) NOT NULL,
    [userId] [int] NOT NULL,
    [amount] [decimal](12, 2) NOT NULL,
    [month] [tinyint] NOT NULL,
    [year] [smallint] NOT NULL,
    [createdAt] [datetime] NULL,
    PRIMARY KEY CLUSTERED
(
[salaryId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
    CONSTRAINT [UQ_Salary_UserMonthYear] UNIQUE NONCLUSTERED
(
    [userId] ASC,
    [month] ASC,
[year] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  View [dbo].[vw_MonthlySummary]    Script Date: 09/05/2026 14:45:46 ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO

-- Full monthly summary
CREATE VIEW [dbo].[vw_MonthlySummary]
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
/****** Object:  Table [dbo].[MonthlyReports]    Script Date: 09/05/2026 14:45:46 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[MonthlyReports](
    [reportId] [int] IDENTITY(1,1) NOT NULL,
    [userId] [int] NOT NULL,
    [month] [tinyint] NOT NULL,
    [year] [smallint] NOT NULL,
    [salary] [decimal](12, 2) NOT NULL,
    [totalExpenses] [decimal](12, 2) NOT NULL,
    [remaining]  AS ([salary]-[totalExpenses]),
    [generatedAt] [datetime] NULL,
    PRIMARY KEY CLUSTERED
(
[reportId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
    CONSTRAINT [UQ_Report_UserMonthYear] UNIQUE NONCLUSTERED
(
    [userId] ASC,
    [month] ASC,
[year] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Index [IX_Expenses_UserDate]    Script Date: 09/05/2026 14:45:46 ******/
CREATE NONCLUSTERED INDEX [IX_Expenses_UserDate] ON [dbo].[Expenses]
(
	[userId] ASC,
	[expenseDate] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Expenses] ADD  DEFAULT (CONVERT([date],getdate())) FOR [expenseDate]
    GO
ALTER TABLE [dbo].[Expenses] ADD  DEFAULT (getdate()) FOR [createdAt]
    GO
ALTER TABLE [dbo].[MonthlyReports] ADD  DEFAULT (getdate()) FOR [generatedAt]
    GO
ALTER TABLE [dbo].[Salaries] ADD  DEFAULT (getdate()) FOR [createdAt]
    GO
ALTER TABLE [dbo].[Users] ADD  DEFAULT (getdate()) FOR [createdAt]
    GO
ALTER TABLE [dbo].[Users] ADD  DEFAULT ('USER') FOR [role]
    GO
ALTER TABLE [dbo].[Expenses]  WITH CHECK ADD  CONSTRAINT [FK_Expenses_Categories] FOREIGN KEY([categoryId])
    REFERENCES [dbo].[Categories] ([categoryId])
    GO
ALTER TABLE [dbo].[Expenses] CHECK CONSTRAINT [FK_Expenses_Categories]
    GO
ALTER TABLE [dbo].[Expenses]  WITH CHECK ADD  CONSTRAINT [FK_Expenses_Users] FOREIGN KEY([userId])
    REFERENCES [dbo].[Users] ([userId])
    ON DELETE CASCADE
GO
ALTER TABLE [dbo].[Expenses] CHECK CONSTRAINT [FK_Expenses_Users]
    GO
ALTER TABLE [dbo].[MonthlyReports]  WITH CHECK ADD  CONSTRAINT [FK_Reports_Users] FOREIGN KEY([userId])
    REFERENCES [dbo].[Users] ([userId])
    ON DELETE CASCADE
GO
ALTER TABLE [dbo].[MonthlyReports] CHECK CONSTRAINT [FK_Reports_Users]
    GO
ALTER TABLE [dbo].[Salaries]  WITH CHECK ADD  CONSTRAINT [FK_Salaries_Users] FOREIGN KEY([userId])
    REFERENCES [dbo].[Users] ([userId])
    ON DELETE CASCADE
GO
ALTER TABLE [dbo].[Salaries] CHECK CONSTRAINT [FK_Salaries_Users]
    GO
ALTER TABLE [dbo].[Expenses]  WITH CHECK ADD CHECK  (([amount]>(0)))
    GO
ALTER TABLE [dbo].[MonthlyReports]  WITH CHECK ADD CHECK  (([month]>=(1) AND [month]<=(12)))
    GO
ALTER TABLE [dbo].[Salaries]  WITH CHECK ADD CHECK  (([amount]>=(0)))
    GO
ALTER TABLE [dbo].[Salaries]  WITH CHECK ADD CHECK  (([month]>=(1) AND [month]<=(12)))
    GO
ALTER TABLE [dbo].[Salaries]  WITH CHECK ADD CHECK  (([year]>=(2000)))
    GO
/****** Object:  StoredProcedure [dbo].[sp_AddExpense]    Script Date: 09/05/2026 14:45:46 ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO

-- Add expense
CREATE   PROCEDURE [dbo].[sp_AddExpense]
    @userId      INT,
    @categoryId  INT,
    @amount      DECIMAL(12,2),
    @description NVARCHAR(200) = NULL,
    @date        DATE = NULL
AS
BEGIN

INSERT INTO Expenses
(
    userId,
    categoryId,
    amount,
    description,
    expenseDate
)
VALUES
    (
        @userId,
        @categoryId,
        @amount,
        @description,
        ISNULL(@date, CAST(GETDATE() AS DATE))
    );

END;

GO
/****** Object:  StoredProcedure [dbo].[sp_GetMonthlySummary]    Script Date: 09/05/2026 14:45:46 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- Get monthly summary
CREATE   PROCEDURE [dbo].[sp_GetMonthlySummary]
    @userId INT,
    @month  TINYINT,
    @year   SMALLINT
AS
BEGIN

SELECT *
FROM vw_MonthlySummary
WHERE userId = @userId
          AND month = @month
          AND year = @year;

END;

GO
/****** Object:  StoredProcedure [dbo].[sp_UpsertSalary]    Script Date: 09/05/2026 14:45:46 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- ============================================================
-- 7. STORED PROCEDURES
-- ============================================================

-- Add or update salary
CREATE   PROCEDURE [dbo].[sp_UpsertSalary]
    @userId INT,
    @amount DECIMAL(12,2),
    @month  TINYINT,
    @year   SMALLINT
AS
BEGIN

    IF EXISTS (
        SELECT 1
        FROM Salaries
        WHERE userId = @userId
          AND month = @month
          AND year = @year
    )
BEGIN
UPDATE Salaries
SET amount = @amount
WHERE userId = @userId
          AND month = @month
          AND year = @year;
END
ELSE
BEGIN
INSERT INTO Salaries(userId, amount, month, year)
VALUES(@userId, @amount, @month, @year);
END

END;

GO
USE [master]
GO
ALTER DATABASE [FinanceTracker] SET  READ_WRITE
GO
