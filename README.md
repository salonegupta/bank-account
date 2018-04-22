# bank-account

### Introduction
The goal of this mini project is to write a simple micro web service to mimic a “Bank Account”.
Through this web service, one can query about the balance, deposit money, and withdraw
money. Just like any Bank, there are restrictions on how many transactions/amounts it can
handle. The details are described below.

- Program should have 3 REST API endpoints: One to get the current balance,
one to make a deposit, and one to make a withdrawal
- No requirement for authentication - assume the web service is for one account
only and is open to the world
- No requirement for the backend store - you can store it in a file or database (your
decision)
- Balance endpoint - this will return the outstanding balance
- Deposit endpoint - credits the account with the specified amount
  - Max deposit for the day = $150K
  - Max deposit per transaction = $40K
  - Max deposit frequency = 4 transactions/day
- Withdrawal endpoint - deducts the account with the specified amount
  - Max withdrawal for the day = $50K
  - Max withdrawal per transaction = $20K
  - Max withdrawal frequency = 3 transactions/day
  - Cannot withdraw when balance is less than withdrawal amount

### How to run the project? ###
* Execute `./gradlew bootRun` from Project root directory
* Internet Connection needed to donwload jars from Maven Repo

### Rest Endpoints
#### http://localhost:8080/v1/accounts/GLOBAL/balance 				[GET]
#### http://localhost:8080/v1/accounts/GLOBAL/deposits        [POST]
#### http://localhost:8080/v1/accounts/GLOBAL/withdrawals     [POST]
#### http://localhost:8080/v1/accounts/GLOBAL/transactions/1     [GET]

Here, GLOBAL is an account number for the Global account.

### Executing API calls using Curl
#### To check account balance:
`curl http://localhost:8080/v1/accounts/GLOBAL/balance`
#### To deposit amount in the account:
`curl -H "Content-Type: application/json" -X POST -d '{"amount":25000}' http://localhost:8080/v1/accounts/GLOBAL/deposits`
#### To withdraw amount from the account:
`curl -H "Content-Type: application/json" -X POST -d '{"amount":15000}' http://localhost:8080/v1/accounts/GLOBAL/withdrawals`
#### To check transaction details for the account:
`curl http://localhost:8080/v1/accounts/GLOBAL/transactions/1`


### Running tests
* Execute `./gradlew test`
* Test Reports `build/reports/tests/index.html`
* Code Coverage `build/reports/coverage/index.html`
