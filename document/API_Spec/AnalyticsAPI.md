# Analytics API Specification

## Overview

AI-powered analytics endpoints for business intelligence. These endpoints proxy to an internal Python microservice that performs data analysis using Prophet (forecasting), mlxtend (association rules), and custom algorithms.

**Base URL**: `/api/v1/analytics`

**Authentication**: Required (Bearer token)

**Authorization**: Admin, Manager, WarehouseStaff roles

---

## Endpoints

### 1. GET /margins

**Description**: Get comprehensive margin analysis across all products.

**Response**:
```json
{
  "status": "OK" | "INSUFFICIENT_DATA" | "ERROR",
  "message": "string",
  "generatedAt": "2025-01-15T10:30:00Z",
  "alerts": [
    {
      "productId": "uuid",
      "productName": "string",
      "currentMargin": 15.5,
      "targetMargin": 20.0,
      "suggestedPrice": 45000,
      "reason": "Below target margin",
      "severity": "low" | "medium" | "high"
    }
  ],
  "summary": {
    "averageMargin": 18.5,
    "belowTargetCount": 12,
    "aboveTargetCount": 45,
    "atRiskRevenue": 5000000
  }
}
```

---

### 2. GET /margins/alerts

**Description**: Get only margin alerts (products needing price adjustment).

**Response**:
```json
[
  {
    "productId": "uuid",
    "productName": "string",
    "currentMargin": 15.5,
    "targetMargin": 20.0,
    "suggestedPrice": 45000,
    "reason": "Below target margin",
    "severity": "low" | "medium" | "high"
  }
]
```

---

### 3. GET /demand/{productId}

**Description**: Get demand analysis for a specific product including sales patterns, trends, and forecasts.

**Path Parameters**:
- `productId` (UUID, required): Product identifier

**Response**:
```json
{
  "status": "OK" | "INSUFFICIENT_DATA" | "ERROR",
  "message": "string",
  "productId": "uuid",
  "productName": "string",
  "dataRange": {
    "start": "2025-01-01",
    "end": "2025-01-15",
    "days": 14
  },
  "averageDailySales": 12.5,
  "weeklyPattern": {
    "monday": 10,
    "tuesday": 8,
    "wednesday": 12,
    "thursday": 15,
    "friday": 20,
    "saturday": 25,
    "sunday": 18
  },
  "trend": "increasing" | "stable" | "decreasing",
  "forecast": [
    {
      "date": "2025-01-16",
      "predicted": 14,
      "lowerBound": 10,
      "upperBound": 18
    }
  ]
}
```

---

### 4. GET /demand/reorder-alerts

**Description**: Get reorder alerts for products that need restocking, prioritized by urgency.

**Response**:
```json
{
  "status": "OK" | "INSUFFICIENT_DATA" | "ERROR",
  "message": "string",
  "generatedAt": "2025-01-15T10:30:00Z",
  "alerts": [
    {
      "productId": "uuid",
      "productName": "string",
      "currentStock": 15,
      "reorderPoint": 50,
      "suggestedQuantity": 100,
      "daysUntilStockout": 3,
      "avgDailySales": 5.2,
      "urgency": "critical" | "warning" | "info"
    }
  ],
  "totalCritical": 3,
  "totalWarning": 8,
  "totalInfo": 12
}
```

---

### 5. GET /bundles

**Description**: Get product bundle/association analysis using Apriori algorithm.

**Response**:
```json
{
  "status": "OK" | "INSUFFICIENT_DATA" | "ERROR",
  "message": "string",
  "generatedAt": "2025-01-15T10:30:00Z",
  "orderCount": 500,
  "uniqueProducts": 120,
  "rules": [
    {
      "antecedent": ["Product A", "Product B"],
      "consequent": ["Product C"],
      "support": 0.15,
      "confidence": 0.75,
      "lift": 2.3
    }
  ],
  "placementSuggestions": [
    {
      "productA": "Milk",
      "productB": "Bread",
      "coOccurrenceCount": 45,
      "suggestion": "Place near each other in store"
    }
  ]
}
```

---

### 6. GET /bundles/data-status

**Description**: Check if there's enough order data for bundle analysis.

**Response**:
```json
{
  "status": "OK" | "INSUFFICIENT_DATA" | "ERROR",
  "message": "string",
  "orderCount": 150,
  "minOrdersRequired": 100,
  "hasEnoughData": true
}
```

---

### 7. GET /health

**Description**: Check AI analytics service health.

**Response**:
```json
{
  "status": "healthy",
  "service": "promart-ai-service",
  "version": "0.1.0",
  "database": "connected",
  "uptime": 3600
}
```

---

## Status Codes

| Status | Meaning |
|--------|---------|
| OK | Data analysis completed successfully |
| INSUFFICIENT_DATA | Not enough historical data for meaningful analysis |
| ERROR | Analysis failed due to an error |

## Error Handling

When the AI service is unavailable, all endpoints return graceful fallback responses:

```json
{
  "status": "ERROR",
  "message": "AI analytics service unavailable",
  ...
}
```

The frontend handles these gracefully with appropriate UI states.

---

## Architecture

```
Frontend → Spring Boot Proxy → Python AI Service → PostgreSQL
              (Auth/RBAC)        (Prophet/mlxtend)
```

- **Spring Boot**: Handles authentication, authorization, and proxies requests
- **Python Service**: Performs actual data analysis (Prophet, Apriori algorithms)
- **Database**: Same PostgreSQL instance used by the main application

---

## Configuration

### Backend (application.yml)
```yaml
analytics:
  service:
    url: http://localhost:8090
    enabled: true
```

### Python Service (.env)
```env
DATABASE_URL=postgresql+asyncpg://postgres:password@localhost:5432/fivetpromart_db
LOG_LEVEL=INFO
```

---

## Security

- All endpoints require authentication (Bearer token)
- Role-based access: Admin, Manager, WarehouseStaff
- Python service is internal-only (not exposed to internet)
- No sensitive data in analytics responses (product names/IDs only)
