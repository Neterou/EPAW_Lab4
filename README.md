# 🫧 BubbleNet — EPAW Lab 4

BubbleNet is a **location-based social network**: users are automatically assigned to
hyper-local community channels — **Bubbles** — based on their registration location.
This Lab 4 build implements the Seminar 3 data model and the Seminar 4 visual design on
top of the asynchronous (AJAX / SPA) MVC architecture.

---

## 🛠️ How to run

1. Open a terminal in the project root.
2. Start the server:
   ```bash
   mvn jetty:run
   ```
3. Open <http://localhost:8080>.

The SQLite database (`lab4.db`) is created automatically from [`DB.txt`](DB.txt) on first
run, including seed bubbles and two demo accounts.

### Demo accounts
| Role  | Username     | Password    |
|-------|--------------|-------------|
| Admin | `admin_demo` | `Admin123!` |
| User  | `user_demo`  | `User123!`  |

> Delete `lab4.db` to reset to the seed data.

---

## ✨ Features

- **Accounts** — full registration (name, email, DNI, location, picture…) with client- and
  server-side validation, SHA-256 password hashing, and automatic Bubble assignment by
  location. Login creates a persistent, `HttpOnly` session.
- **Interactive map (Leaflet)** — the home page is a split view: an OpenStreetMap of all
  bubbles on the left, the selected bubble's live feed on the right. Markers are custom
  `divIcon`s coloured by category, sized by `√(member count)`, with a pulsing gold ring on
  your own bubble.
- **Bubble membership** — users can belong to many bubbles. Join / leave from the **Discover ›
  Bubbles** tab, the map marker popup, or the feed header. Open bubbles approve instantly;
  closed bubbles create a PENDING request. Membership gates participation (you must be an
  approved member to like or reply).
- **Role-based feed** — admins broadcast posts (Standard / Official / Alert / Warning, each
  with its own left-border accent); members get a locked compose box but can **reply** to
  any post.
- **Engagement** — like / unlike posts and threaded replies (comments), all over AJAX.
- **Social graph** — follow / unfollow other members (Buddies / Discover › People).
- **Dynamic navbar** — adapts to the visitor's role (anonymous / user / admin).

## 🧱 Architecture

```
controller/   Servlets (one per action)         model/        Plain beans: User, Bubble, Tweet, Comment
service/      Business logic (validation,        repository/   SQL data access (one per table)
              hashing, bubble assignment)        util/         DBManager, filters, session config
webapp/       index.html (SPA shell) + JSP fragments + css/ + js/
```

Every AJAX call hits a Controller → Service → Repository, and returns an HTML fragment
(or JSON for the map / like endpoints) that is injected into the page.

---

*Universitat Pompeu Fabra · Software Engineering of Web Applications · 2026*
