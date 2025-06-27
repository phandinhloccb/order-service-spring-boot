### Exception Handling
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Controller    │    │   Service Layer  │    │ External APIs   │
│                 │    │                  │    │                 │
│ HTTP Requests   │───▶│ Business Logic   │───▶│ Inventory API   │
└─────────────────┘    └──────────────────┘    └─────────────────┘
         │                       │                      │
         │              ┌────────▼────────┐             │
         │              │ Domain          │             │
         │              │ Exceptions      │             │
         │              │ • Business      │             │
         │              │ • Validation    │             │
         │              └─────────────────┘             │
         │                                              │
         │              ┌─────────────────┐             │
         │              │ Infrastructure  │◀────────────┘
         │              │ Exceptions      │
         │              │ • Network       │
         │              │ • Timeout       │
         │              │ • External APIs │
         │              └─────────────────┘
         │                       │
         ▼                       ▼
┌─────────────────────────────────────────┐
│        Global Exception Handler         │
│                                         │
│ • Catch all exceptions                  │
│ • Map to HTTP status codes             │
│ • Create standardized error responses  │
│ • Log errors for monitoring            │
└─────────────────────────────────────────┘
         │
         ▼
┌─────────────────┐
│   HTTP Client   │
│                 │
│ Standardized    │
│ Error Response  │
└─────────────────┘