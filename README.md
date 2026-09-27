# E-Commerce Shopping App (Android Kotlin) 🛍️📱

A modern, full-featured **E-Commerce Shopping Mobile Application** for Android built natively using **Kotlin**, **Android SDK**, and **SQLite Local Database**.

---

## 🌟 Key Features

- **🔐 User Authentication:**
  - User Registration & Login with validation
  - Default guest user support
  - Session and profile management

- **🛍️ Product Browsing & Discovery:**
  - Modern home screen with promotion banners
  - Category filtering (All, Shoes, Shoulder bags, Clothes, etc.)
  - Real-time product search by name and brand
  - Popular & discounted products showcase

- **📄 Product Details & Favorites:**
  - Detailed product view with high-resolution images, descriptions, ratings, and pricing
  - Add to Favorites with one-tap heart icon toggle
  - Dedicated Favorites screen

- **🛒 Shopping Cart & Management:**
  - Dynamic shopping cart with quantity increment/decrement
  - Real-time subtotal and total calculation
  - Easy item removal

- **💳 Multi-step Checkout Process:**
  - Shipping address selection & form
  - Payment method options (Credit/Debit Card, Bakong KHQR / Cash on Delivery)
  - Order summary confirmation

- **📦 Order History:**
  - Track placed orders and review order details

---

## 🛠️ Technology Stack & Architecture

- **Language:** [Kotlin](https://kotlinlang.org/) (100% Native)
- **Target SDK:** Android SDK 37 (Min SDK 24)
- **UI Framework:** Material Design 3, Android ViewBinding
- **Navigation:** Single Activity Architecture with Android Navigation Components & Fragments
- **Local Database:** SQLite via `SQLiteOpenHelper` (`DatabaseHelper.kt`) for offline data persistence
- **Build System:** Gradle with Kotlin DSL (`build.gradle.kts`)

---

## 📁 Project Structure

```
app/src/main/java/com/example/ecommerceshoppingapp1/
├── adapter/          # RecyclerView Adapters (Product, Cart, Order, Category, etc.)
├── database/         # SQLiteOpenHelper & Database operations
├── model/            # Data Models (Product, User, CartItem, Order, Category)
├── ui/               # Activities & UI Fragments (Home, Detail, Cart, Checkout, etc.)
└── utils/            # Extension functions and helper utilities
```

---

## 🚀 Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/som-chanchav/ECommerceShoppingApp-Android-Kotlin.git
   ```
2. **Open in Android Studio:**
   - Launch Android Studio (Ladybug / Koala or newer)
   - Select **Open** and choose the cloned folder
3. **Build & Run:**
   - Let Gradle sync dependencies
   - Run on an Android Emulator (API 24+) or physical device

---

## 👨‍💻 Author

- **Developer:** Som Chanchav
- **Portfolio:** [som-chanchav.netlify.app](https://som-chanchav.netlify.app/)
