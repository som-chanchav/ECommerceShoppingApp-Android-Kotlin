package com.example.ecommerceshoppingapp1.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import com.example.ecommerceshoppingapp1.model.CartItem
import com.example.ecommerceshoppingapp1.model.Product

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, "EcommerceApp.db", null, 21) {

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                email TEXT UNIQUE,
                password TEXT
            )
        """.trimIndent()
        db.execSQL(createUsersTable)
        
        // insert default guest user
        val guestValues = ContentValues().apply {
            put("name", "Guest User")
            put("email", "guest@gmail.com")
            put("password", "123")
        }
        db.insert("users", null, guestValues)

        val createProductsTable = """
            CREATE TABLE products (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT,
                price REAL,
                category TEXT,
                imagePath TEXT,
                rating REAL,
                description TEXT,
                discount INTEGER,
                isPopular INTEGER,
                isFavorite INTEGER DEFAULT 0
            )
        """.trimIndent()
        db.execSQL(createProductsTable)

        val createCartTable = """
            CREATE TABLE cart_items (
                cartId INTEGER PRIMARY KEY AUTOINCREMENT,
                productId INTEGER,
                quantity INTEGER,
                size TEXT,
                color TEXT
            )
        """.trimIndent()
        db.execSQL(createCartTable)

        val createOrdersTable = """
            CREATE TABLE orders (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_email TEXT,
                product_title TEXT,
                price REAL,
                image_path TEXT,
                quantity INTEGER,
                order_date TEXT,
                size TEXT,
                color TEXT
            )
        """.trimIndent()
        db.execSQL(createOrdersTable)

        // 1. Insert original special products from "products" folder
        try {
            val files = context.assets.list("products")
            files?.forEachIndexed { index, fileName ->
                if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                    insertDummyProduct(
                        db,
                        title = "Special Product ${index + 1}",
                        price = 25.0 + (index * 5),
                        category = getCategoryFromFileName(fileName),
                        imagePath = "products/$fileName",
                        rating = 4.0f + (index % 10) * 0.1f,
                        description = "This is a great high-quality product that you will love.",
                        discount = if (index % 2 == 0) 10 + index * 5 else 0,
                        isPopular = 0
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 2. Insert original popular products from "PopularProducts/PopularProducts" folder
        try {
            val popularFiles = context.assets.list("PopularProducts/PopularProducts")
            var popIndex = 1
            popularFiles?.forEachIndexed { index, fileName ->
                if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                    val title = "Popular Item $popIndex"
                    popIndex++
                    
                    if (title == "Popular Item 4" || title == "Popular Item 8") {
                        return@forEachIndexed
                    }

                    insertDummyProduct(
                        db,
                        title = title,
                        price = 45.0 + (index * 10.0),
                        category = getCategoryFromFileName(fileName),
                        imagePath = "PopularProducts/PopularProducts/$fileName",
                        rating = 4.5f + (index % 5) * 0.1f,
                        description = "This is a premium trending product from our popular collection.",
                        discount = if (index % 3 == 0) 15 else 0,
                        isPopular = 1
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 3. Insert new custom category products from folder-specific assets
        val categoryFolders = mapOf(
            "Shoes" to "Shoes",
            "bag" to "Shoulder bag",
            "fashion" to "Women's Fashion",
            "jewelry" to "Jewelry"
        )

        for ((folder, categoryName) in categoryFolders) {
            try {
                val files = context.assets.list(folder)
                files?.forEachIndexed { index, fileName ->
                    if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                        val actualCategory = if (folder == "fashion") {
                            if (index % 2 == 0) "Women's Fashion" else "Men's Fashion"
                        } else {
                            categoryName
                        }
                        
                        val isPopular = if (index % 2 == 1) 1 else 0
                        val displayFolder = folder.replaceFirstChar { it.uppercase() }
                        val title = "$displayFolder Item ${index + 1}"
                        
                        if (title == "Fashion Item 4") {
                            return@forEachIndexed
                        }
                        
                        insertDummyProduct(
                            db,
                            title = title,
                            price = 29.99 + (index * 15.0),
                            category = actualCategory,
                            imagePath = "$folder/$fileName",
                            rating = 4.0f + (index % 5) * 0.2f,
                            description = "This is a premium $actualCategory item selected for you. It features high quality materials and durability.",
                            discount = if (index % 2 == 0) 10 + index * 5 else 0,
                            isPopular = isPopular
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getCategoryFromFileName(fileName: String): String {
        val lower = fileName.lowercase()
        return when {
            lower.contains("shoes") -> "Shoes"
            lower.contains("bag") || lower.contains("shoulder") -> "Shoulder bag"
            lower.contains("women") -> "Women's Fashion"
            lower.contains("men") -> "Men's Fashion"
            lower.contains("jewelry") || lower.matches(Regex("^(1|12|14|15|16|18)\\.(jpg|png)$")) -> "Jewelry"
            else -> {
                val cleanName = fileName.substringBeforeLast(".")
                val number = cleanName.filter { it.isDigit() }.toIntOrNull()
                if (number != null) {
                    val categories = listOf("Shoes", "Shoulder bag", "Women's Fashion", "Jewelry", "Men's Fashion")
                    categories[number % categories.size]
                } else {
                    val hash = fileName.hashCode().coerceAtLeast(0)
                    val categories = listOf("Shoes", "Shoulder bag", "Women's Fashion", "Jewelry", "Men's Fashion")
                    categories[hash % categories.size]
                }
            }
        }
    }

    private fun insertDummyProduct(db: SQLiteDatabase, title: String, price: Double, category: String, imagePath: String, rating: Float, description: String, discount: Int, isPopular: Int) {
        val values = ContentValues().apply {
            put("title", title)
            put("price", price)
            put("category", category)
            put("imagePath", imagePath)
            put("rating", rating)
            put("description", description)
            put("discount", discount)
            put("isPopular", isPopular)
        }
        db.insert("products", null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS products")
        db.execSQL("DROP TABLE IF EXISTS cart_items")
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS orders")
        onCreate(db)
    }

    fun getCartItems(): MutableList<CartItem> {
        val cartList = mutableListOf<CartItem>()
        val db = this.readableDatabase
        val query = """
            SELECT c.cartId, c.quantity as cart_qty, c.size, c.color,
                   p.* 
            FROM cart_items c 
            JOIN products p ON c.productId = p.id
        """.trimIndent()
        
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val product = Product(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                    category = cursor.getString(cursor.getColumnIndexOrThrow("category")),
                    imagePath = cursor.getString(cursor.getColumnIndexOrThrow("imagePath")),
                    rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    discount = cursor.getInt(cursor.getColumnIndexOrThrow("discount")),
                    isPopular = cursor.getInt(cursor.getColumnIndexOrThrow("isPopular")) == 1,
                    isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("isFavorite")) == 1
                )
                val cartItem = CartItem(
                    cartId = cursor.getInt(cursor.getColumnIndexOrThrow("cartId")),
                    product = product,
                    quantity = cursor.getInt(cursor.getColumnIndexOrThrow("cart_qty")),
                    size = cursor.getString(cursor.getColumnIndexOrThrow("size")),
                    color = cursor.getString(cursor.getColumnIndexOrThrow("color"))
                )
                cartList.add(cartItem)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return cartList
    }

    fun updateCartQuantity(cartId: Int, quantity: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply { put("quantity", quantity) }
        return db.update("cart_items", values, "cartId = ?", arrayOf(cartId.toString())) > 0
    }

    fun deleteCartItem(cartId: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("cart_items", "cartId = ?", arrayOf(cartId.toString())) > 0
    }

    fun clearCart(): Boolean {
        val db = this.writableDatabase
        return db.delete("cart_items", null, null) > 0
    }

    fun getCartCount(): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT SUM(quantity) FROM cart_items", null)
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }

    fun getProductById(id: Int): Product? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM products WHERE id = ?", arrayOf(id.toString()))
        var product: Product? = null
        if (cursor.moveToFirst()) {
            product = Product(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                price = cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                category = cursor.getString(cursor.getColumnIndexOrThrow("category")),
                imagePath = cursor.getString(cursor.getColumnIndexOrThrow("imagePath")),
                rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating")),
                description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                discount = cursor.getInt(cursor.getColumnIndexOrThrow("discount")),
                isPopular = cursor.getInt(cursor.getColumnIndexOrThrow("isPopular")) == 1,
                isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("isFavorite")) == 1
            )
        }
        cursor.close()
        return product
    }

    fun addToCart(productId: Int, quantity: Int, size: String, color: String): Boolean {
        val db = this.writableDatabase
        
        // Check if item with same productId, size and color already exists
        val cursor = db.rawQuery("SELECT cartId, quantity FROM cart_items WHERE productId = ? AND size = ? AND color = ?", arrayOf(productId.toString(), size, color))
        if (cursor.moveToFirst()) {
            val cartId = cursor.getInt(0)
            val existingQty = cursor.getInt(1)
            val newQty = existingQty + quantity
            cursor.close()
            return updateCartQuantity(cartId, newQty)
        }
        cursor.close()

        val values = ContentValues().apply {
            put("productId", productId)
            put("quantity", quantity)
            put("size", size)
            put("color", color)
        }
        return db.insert("cart_items", null, values) != -1L
    }

    fun getAllProducts(): List<Product> {
        val productList = mutableListOf<Product>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM products", null)
        if (cursor.moveToFirst()) {
            do {
                val product = Product(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                    category = cursor.getString(cursor.getColumnIndexOrThrow("category")),
                    imagePath = cursor.getString(cursor.getColumnIndexOrThrow("imagePath")),
                    rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    discount = cursor.getInt(cursor.getColumnIndexOrThrow("discount")),
                    isPopular = cursor.getInt(cursor.getColumnIndexOrThrow("isPopular")) == 1,
                    isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("isFavorite")) == 1
                )
                productList.add(product)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return productList
    }
    
    fun checkUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", arrayOf(email, password))
        val count = cursor.count
        cursor.close()
        return count > 0
    }
    
    fun registerUser(name: String, email: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("email", email)
            put("password", password)
        }
        val result = db.insert("users", null, values)
        return result != -1L
    }

    fun getUserByEmail(email: String): com.example.ecommerceshoppingapp1.model.User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", arrayOf(email))
        if (cursor.moveToFirst()) {
            val user = com.example.ecommerceshoppingapp1.model.User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            )
            cursor.close()
            return user
        }
        cursor.close()
        return null
    }
    
    fun toggleFavorite(productId: Int, isFavorite: Boolean): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("isFavorite", if (isFavorite) 1 else 0)
        }
        val rows = db.update("products", values, "id = ?", arrayOf(productId.toString()))
        return rows > 0
    }
    
    fun getFavoriteProducts(): List<Product> {
        val productList = mutableListOf<Product>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM products WHERE isFavorite = 1", null)
        if (cursor.moveToFirst()) {
            do {
                val product = Product(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                    category = cursor.getString(cursor.getColumnIndexOrThrow("category")),
                    imagePath = cursor.getString(cursor.getColumnIndexOrThrow("imagePath")),
                    rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    discount = cursor.getInt(cursor.getColumnIndexOrThrow("discount")),
                    isPopular = cursor.getInt(cursor.getColumnIndexOrThrow("isPopular")) == 1,
                    isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("isFavorite")) == 1
                )
                productList.add(product)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return productList
    }

    fun saveOrder(userEmail: String, productTitle: String, price: Double, imagePath: String, quantity: Int, size: String, color: String): Boolean {
        val db = this.writableDatabase
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
        val currentDate = sdf.format(java.util.Date())
        
        val values = ContentValues().apply {
            put("user_email", userEmail)
            put("product_title", productTitle)
            put("price", price)
            put("image_path", imagePath)
            put("quantity", quantity)
            put("order_date", currentDate)
            put("size", size)
            put("color", color)
        }
        return db.insert("orders", null, values) != -1L
    }

    fun getUserOrders(userEmail: String): List<com.example.ecommerceshoppingapp1.model.Order> {
        val orderList = mutableListOf<com.example.ecommerceshoppingapp1.model.Order>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM orders WHERE user_email = ? ORDER BY id DESC", arrayOf(userEmail))
        if (cursor.moveToFirst()) {
            do {
                val order = com.example.ecommerceshoppingapp1.model.Order(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    userEmail = cursor.getString(cursor.getColumnIndexOrThrow("user_email")),
                    productTitle = cursor.getString(cursor.getColumnIndexOrThrow("product_title")),
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                    imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image_path")),
                    quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                    orderDate = cursor.getString(cursor.getColumnIndexOrThrow("order_date")),
                    size = cursor.getString(cursor.getColumnIndexOrThrow("size")),
                    color = cursor.getString(cursor.getColumnIndexOrThrow("color"))
                )
                orderList.add(order)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return orderList
    }
}
