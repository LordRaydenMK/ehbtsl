graph LR
    A[Form Displayed] --> |Submit| B
    B[Validate Form]
    B --> |Error| A
    B --> |Success| H[Call API]
    H --> |Error| A
    H --> J[Store Token]
    J --> K[End]