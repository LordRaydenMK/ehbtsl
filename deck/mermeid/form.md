graph LR
    A[Form Displayed] --> |Submit Form| B
    B[Validate Form]
    B --> D[Validate Name] --> G
    B -->E[Validate Email or Phone] --> G
    G[Form Result]
    G --> |Success| H[Valid Form]
    G --> |Error| A
