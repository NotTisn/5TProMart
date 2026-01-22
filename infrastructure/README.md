# 5TProMart Infrastructure

> **"Clone. `dev`. Code."**

Zero setup. Zero navigation. Just run.

---

## 🚀 Quick Start

```bash
git clone <repo>
cd 5TProMart
dev
```

That's it. Docker starts PostgreSQL + Keycloak, waits for health, launches Spring Boot with hot reload.

### ⚙️ First-Time Setup

```bash
cd 5TProMart_be
cp .env.example .env
```

That's it. The example has the correct Keycloak client secret from the shared realm.

---

## 📁 Directory Structure

```
5TProMart/
├── dev.bat                     ← THE BUTTON. Run from root.
├── src/                        ← Your code
├── pom.xml
│
└── infrastructure/             ← Docker + scripts
    ├── compose-infra-only.yaml
    ├── compose.yaml
    ├── Dockerfile
    ├── README.md               ← You are here
    │
    ├── scripts/
    │   ├── stop_all.bat
    │   ├── restart.bat
    │   ├── clean.bat
    │   ├── logs.bat
    │   └── status.bat
    │
    ├── keycloak-config/
    │   └── fivetpro-realm.json
    │
    ├── seed/
    │   ├── init.sql
    │   ├── seed.sql
    │   └── rinse-and-seed.bat
    │
    └── data/                   ← Docker volumes (gitignored)
```

---

## 🔧 Available Commands

All commands run from project root:

| Command | Description |
|---------|-------------|
| `dev` | Start everything |
| `dev --skip-app` | Only start infrastructure |
| `dev --stop` | Stop Java services (keep Docker) |
| `dev --stop --docker` | Stop everything |
| `dev --status` | Check all services |
| `dev --clean` | Nuclear reset (delete all data) |

**Advanced** (from infrastructure/scripts):

| Command | Description |
|---------|-------------|
| `restart api` | Restart Spring Boot |
| `restart keycloak` | Restart Keycloak |
| `logs keycloak` | View Keycloak logs |

---

## 🌐 Service URLs

| Service | URL | Credentials |
|---------|-----|-------------|
| **5TProMart API** | http://localhost:8080 | - |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | - |
| **PostgreSQL** | localhost:5432/fivetpromart_db | postgres / votrungtin2005 |
| **Keycloak Admin** | http://localhost:8180/admin | admin / admin |
| **Keycloak Realm** | http://localhost:8180/realms/fivetpro | - |

---

## 👤 Test Users (Keycloak)

| Username | Password | Role | Description |
|----------|----------|------|-------------|
| `admin` | `admin123` | Admin | Full system access |
| `manager` | `manager123` | Manager | Read access, reports |
| `salesstaff` | `sales123` | SalesStaff | Orders, customers, POS |
| `warehousestaff` | `warehouse123` | WarehouseStaff | Stock, inventory, suppliers |

**⚠️ Important**: Role names are **PascalCase** and **case-sensitive** (`Admin` not `ADMIN`).

---

## 🔥 Hot Reload

DevTools is already configured. To see changes:

1. Edit code in your IDE
2. Save the file
3. Wait 2-3 seconds
4. DevTools auto-restarts the context

**IntelliJ Setup** (if not working):
1. Settings → Build, Execution, Deployment → Compiler → "Build project automatically" ✓
2. Settings → Advanced Settings → "Allow auto-make to start..." ✓

---

## 🏗️ Architecture

```
┌────────────────────────────────────────────────────────────────────┐
│                          YOUR MACHINE                               │
├────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   ┌─────────────────────────────────────────────────────────────┐  │
│   │                    DOCKER (Infrastructure)                   │  │
│   │                                                              │  │
│   │   ┌──────────────┐              ┌──────────────┐            │  │
│   │   │  PostgreSQL  │              │   Keycloak   │            │  │
│   │   │    :5432     │              │    :8180     │            │  │
│   │   └──────────────┘              └──────────────┘            │  │
│   │                                                              │  │
│   └─────────────────────────────────────────────────────────────┘  │
│                              ↓ localhost ports                      │
│   ┌─────────────────────────────────────────────────────────────┐  │
│   │                 NATIVE (Your Code + IDE)                     │  │
│   │                                                              │  │
│   │   ┌───────────────────────────────────────────────────────┐ │  │
│   │   │              5TProMart Spring Boot API                 │ │  │
│   │   │                      :8080                             │ │  │
│   │   │              (Hot reload with DevTools)                │ │  │
│   │   └───────────────────────────────────────────────────────┘ │  │
│   │                                                              │  │
│   └─────────────────────────────────────────────────────────────┘  │
│                                                                     │
└────────────────────────────────────────────────────────────────────┘
```

**Why Hybrid?**
- **Infrastructure in Docker**: No manual install of PostgreSQL, Keycloak
- **Code runs natively**: Full IDE integration, instant hot reload, native debugging

---

## 🔐 Keycloak Realm

The realm `fivetpro` is auto-imported on first start from `keycloak-config/fivetpro-realm.json`.

**Configuration:**
- Realm: `fivetpro`
- Client ID: `fivetpro`
- Client Secret: `WtaR8BLWkwL6OTWKRZjoGu12yk888onl`
- Direct Access Grants: Enabled (for password auth)
- Service Accounts: Enabled (for admin operations)

**To re-import realm:**
1. Delete `data/keycloak/` folder
2. Run `scripts\restart keycloak`

---

## 🗃️ Database Seeding

**Insert sample data:**
```bash
docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db < seed\seed.sql
```

**Reset database:**
```bash
cd seed
rinse-and-seed
```

---

## 🐳 Full Docker Mode (CI/CD)

For running everything in Docker (no native code):

```bash
docker compose up -d --build
```

This builds the Spring Boot app into a container and runs alongside infrastructure.

---

## ⚠️ Troubleshooting

### Port already in use
```bash
# Find what's using the port
netstat -ano | findstr ":8080"

# Kill it
taskkill /F /PID <PID>
```

### Keycloak realm not found
1. Check `keycloak-config/fivetpro-realm.json` exists
2. Delete `data/keycloak/` and restart
3. Or manually import via admin console

### Database connection refused
1. Run `scripts\status` to check if PostgreSQL is running
2. Check logs: `scripts\logs postgres`
3. Restart: `scripts\restart postgres`

### Hot reload not working
1. Ensure DevTools is in pom.xml (already added)
2. Enable auto-build in IDE
3. Check console for "LiveReload server is running"

### Nuclear option
```bash
scripts\clean
```
This deletes EVERYTHING and lets you start fresh.

---

## 📝 Quick Reference Card

```
┌─────────────────────────────────────────────────────┐
│                FROM PROJECT ROOT                    │
├─────────────────────────────────────────────────────┤
│  dev                  Start everything              │
│  dev --stop           Stop Java (keep Docker)       │
│  dev --stop --docker  Stop everything               │
│  dev --status         Check all services            │
│  dev --clean          Nuclear reset                 │
├─────────────────────────────────────────────────────┤
│                KEY URLS                             │
├─────────────────────────────────────────────────────┤
│  API              http://localhost:8080             │
│  Keycloak Admin   http://localhost:8180/admin       │
│  PostgreSQL       localhost:5432/fivetpromart_db    │
├─────────────────────────────────────────────────────┤
│                DEFAULT CREDS                        │
├─────────────────────────────────────────────────────┤
│  Keycloak Admin   admin / admin                     │
│  Test User        testuser / test123                │
│  Admin User       admin / admin123                  │
│  PostgreSQL       postgres / votrungtin2005         │
└─────────────────────────────────────────────────────┘
```

---

*Infrastructure follows the Hybrid Dev Pattern from HYBRID_DEV_INFRASTRUCTURE_GUIDE.md*
