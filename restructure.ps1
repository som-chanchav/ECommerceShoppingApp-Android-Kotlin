$baseDir = "E:\learn android\ECommerceShoppingApp1\app\src\main\java\com\example\ecommerceshoppingapp1"

$filesToMove = @{
    "Category.kt" = "model";
    "Product.kt" = "model";
    "cart\CartItem.kt" = "model";
    "OnboardingAdapter\OnboardingItem.kt" = "model";
    "CategoryAdapter.kt" = "adapter";
    "ProductAdapter.kt" = "adapter";
    "cart\CartAdapter.kt" = "adapter";
    "OnboardingAdapter\OnboardingAdapter.kt" = "adapter";
    "MainActivity.kt" = "ui";
    "HomepageActivity.kt" = "ui";
    "HomeFragment.kt" = "ui";
    "FavoriteFragment.kt" = "ui";
    "ProfileFragment.kt" = "ui";
    "SecondFragment.kt" = "ui";
    "cart\CartFragment.kt" = "ui";
    "cart\DetailFragment.kt" = "ui";
    "loginregister\LoginFragment.kt" = "ui";
    "loginregister\RegisterFragment.kt" = "ui";
    "OnboardingAdapter\OnboardingFragment.kt" = "ui";
    "Database\DatabaseHelper.kt" = "database";
}

# Create dirs
"model", "adapter", "ui", "database" | ForEach-Object {
    New-Item -ItemType Directory -Force -Path "$baseDir\$_" | Out-Null
}

foreach ($oldPath in $filesToMove.Keys) {
    $newFolder = $filesToMove[$oldPath]
    $oldFullPath = "$baseDir\$oldPath"
    $filename = Split-Path $oldFullPath -Leaf
    $newFullPath = "$baseDir\$newFolder\$filename"

    if (Test-Path $oldFullPath) {
        $content = Get-Content $oldFullPath -Raw
        $newPackage = "package com.example.ecommerceshoppingapp1.$newFolder"
        $content = $content -replace '(?m)^package\s+.*', $newPackage
        Set-Content -Path $newFullPath -Value $content -Encoding UTF8
        Remove-Item $oldFullPath
        Write-Host "Moved $filename to $newFolder"
    } else {
        Write-Host "Warning: $oldFullPath not found."
    }
}

# Remove empty dirs
"cart", "loginregister", "OnboardingAdapter", "Database" | ForEach-Object {
    $dirPath = "$baseDir\$_"
    if (Test-Path $dirPath) {
        Remove-Item $dirPath -Force -Recurse
        Write-Host "Removed $dirPath"
    }
}
