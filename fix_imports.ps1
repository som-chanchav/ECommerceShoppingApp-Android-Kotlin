$baseDir = "E:\learn android\ECommerceShoppingApp1\app\src\main\java\com\example\ecommerceshoppingapp1"
$manifestPath = "E:\learn android\ECommerceShoppingApp1\app\src\main\AndroidManifest.xml"
$navDir = "E:\learn android\ECommerceShoppingApp1\app\src\main\res\navigation"

function Add-Import($filePath, $importString) {
    if (Test-Path $filePath) {
        $content = Get-Content $filePath -Raw
        if ($content -notmatch [regex]::Escape("import $importString")) {
            $content = $content -replace '(?m)^package\s+.*$', "$0`n`nimport $importString"
            Set-Content -Path $filePath -Value $content -Encoding UTF8
        }
    }
}

# UI files
$uiFiles = "HomeFragment.kt", "HomepageActivity.kt", "LoginFragment.kt", "MainActivity.kt", "OnboardingFragment.kt", "RegisterFragment.kt", "CartFragment.kt", "DetailFragment.kt", "FavoriteFragment.kt", "ProfileFragment.kt", "SecondFragment.kt"
foreach ($file in $uiFiles) { Add-Import "$baseDir\ui\$file" "com.example.ecommerceshoppingapp1.R" }

# Specific UI files
Add-Import "$baseDir\ui\HomeFragment.kt" "com.example.ecommerceshoppingapp1.model.Category"
Add-Import "$baseDir\ui\HomeFragment.kt" "com.example.ecommerceshoppingapp1.model.Product"
Add-Import "$baseDir\ui\HomeFragment.kt" "com.example.ecommerceshoppingapp1.adapter.CategoryAdapter"
Add-Import "$baseDir\ui\HomeFragment.kt" "com.example.ecommerceshoppingapp1.adapter.ProductAdapter"

Add-Import "$baseDir\ui\LoginFragment.kt" "com.example.ecommerceshoppingapp1.database.DatabaseHelper"
Add-Import "$baseDir\ui\LoginFragment.kt" "com.example.ecommerceshoppingapp1.ui.HomepageActivity"

Add-Import "$baseDir\ui\OnboardingFragment.kt" "com.example.ecommerceshoppingapp1.adapter.OnboardingAdapter"
Add-Import "$baseDir\ui\OnboardingFragment.kt" "com.example.ecommerceshoppingapp1.model.OnboardingItem"

Add-Import "$baseDir\ui\RegisterFragment.kt" "com.example.ecommerceshoppingapp1.database.DatabaseHelper"

Add-Import "$baseDir\ui\CartFragment.kt" "com.example.ecommerceshoppingapp1.model.CartItem"
Add-Import "$baseDir\ui\CartFragment.kt" "com.example.ecommerceshoppingapp1.adapter.CartAdapter"
Add-Import "$baseDir\ui\CartFragment.kt" "com.example.ecommerceshoppingapp1.database.DatabaseHelper"

# Adapter files
$adapterFiles = "CartAdapter.kt", "CategoryAdapter.kt", "OnboardingAdapter.kt", "ProductAdapter.kt"
foreach ($file in $adapterFiles) { Add-Import "$baseDir\adapter\$file" "com.example.ecommerceshoppingapp1.R" }

Add-Import "$baseDir\adapter\CartAdapter.kt" "com.example.ecommerceshoppingapp1.model.CartItem"
Add-Import "$baseDir\adapter\CategoryAdapter.kt" "com.example.ecommerceshoppingapp1.model.Category"
Add-Import "$baseDir\adapter\OnboardingAdapter.kt" "com.example.ecommerceshoppingapp1.model.OnboardingItem"
Add-Import "$baseDir\adapter\ProductAdapter.kt" "com.example.ecommerceshoppingapp1.model.Product"

# Fix Manifest
if (Test-Path $manifestPath) {
    $manifestContent = Get-Content $manifestPath -Raw
    $manifestContent = $manifestContent -replace 'android:name="\.MainActivity"', 'android:name=".ui.MainActivity"'
    $manifestContent = $manifestContent -replace 'android:name="\.HomepageActivity"', 'android:name=".ui.HomepageActivity"'
    Set-Content -Path $manifestPath -Value $manifestContent -Encoding UTF8
}

# Fix Navigation XMLs
Get-ChildItem -Path $navDir -Filter *.xml | ForEach-Object {
    $navContent = Get-Content $_.FullName -Raw
    # Fix paths
    $navContent = $navContent -replace 'com.example.ecommerceshoppingapp1.cart.', 'com.example.ecommerceshoppingapp1.ui.'
    $navContent = $navContent -replace 'com.example.ecommerceshoppingapp1.loginregister.', 'com.example.ecommerceshoppingapp1.ui.'
    $navContent = $navContent -replace 'com.example.ecommerceshoppingapp1.OnboardingAdapter.', 'com.example.ecommerceshoppingapp1.ui.'
    # For fragments that were in root:
    $navContent = $navContent -replace 'android:name="com.example.ecommerceshoppingapp1.HomeFragment"', 'android:name="com.example.ecommerceshoppingapp1.ui.HomeFragment"'
    $navContent = $navContent -replace 'android:name="com.example.ecommerceshoppingapp1.FavoriteFragment"', 'android:name="com.example.ecommerceshoppingapp1.ui.FavoriteFragment"'
    $navContent = $navContent -replace 'android:name="com.example.ecommerceshoppingapp1.ProfileFragment"', 'android:name="com.example.ecommerceshoppingapp1.ui.ProfileFragment"'
    $navContent = $navContent -replace 'android:name="com.example.ecommerceshoppingapp1.SecondFragment"', 'android:name="com.example.ecommerceshoppingapp1.ui.SecondFragment"'
    
    # Fix tools:context
    $navContent = $navContent -replace 'tools:context="\.', 'tools:context=".ui.'
    
    Set-Content -Path $_.FullName -Value $navContent -Encoding UTF8
}
