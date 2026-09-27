$baseDir = "E:\learn android\ECommerceShoppingApp1\app\src\main\java\com\example\ecommerceshoppingapp1"

$imports = @{
    "Product" = "com.example.ecommerceshoppingapp1.model.Product";
    "Category" = "com.example.ecommerceshoppingapp1.model.Category";
    "CartItem" = "com.example.ecommerceshoppingapp1.model.CartItem";
    "OnboardingItem" = "com.example.ecommerceshoppingapp1.model.OnboardingItem";
    "CategoryAdapter" = "com.example.ecommerceshoppingapp1.adapter.CategoryAdapter";
    "ProductAdapter" = "com.example.ecommerceshoppingapp1.adapter.ProductAdapter";
    "CartAdapter" = "com.example.ecommerceshoppingapp1.adapter.CartAdapter";
    "OnboardingAdapter" = "com.example.ecommerceshoppingapp1.adapter.OnboardingAdapter";
    "DatabaseHelper" = "com.example.ecommerceshoppingapp1.database.DatabaseHelper";
    "HomepageActivity" = "com.example.ecommerceshoppingapp1.ui.HomepageActivity";
    "MainActivity" = "com.example.ecommerceshoppingapp1.ui.MainActivity";
}

Get-ChildItem -Path $baseDir -Filter *.kt -Recurse | ForEach-Object {
    $filePath = $_.FullName
    $content = Get-Content $filePath -Raw

    # Clean old wrong imports
    $content = $content -replace '(?m)^import com\.example\.ecommerceshoppingapp1\.Database\.DatabaseHelper.*$\n?', ''
    $content = $content -replace '(?m)^import com\.example\.ecommerceshoppingapp1\.cart\..*$\n?', ''
    $content = $content -replace '(?m)^import com\.example\.ecommerceshoppingapp1\.OnboardingAdapter\..*$\n?', ''
    $content = $content -replace '(?m)^import com\.example\.ecommerceshoppingapp1\.loginregister\..*$\n?', ''
    
    # Also clean root imports of moved files
    $content = $content -replace '(?m)^import com\.example\.ecommerceshoppingapp1\.Product$\n?', ''
    $content = $content -replace '(?m)^import com\.example\.ecommerceshoppingapp1\.Category$\n?', ''
    $content = $content -replace '(?m)^import com\.example\.ecommerceshoppingapp1\.ProductAdapter$\n?', ''
    $content = $content -replace '(?m)^import com\.example\.ecommerceshoppingapp1\.CategoryAdapter$\n?', ''
    $content = $content -replace '(?m)^import com\.example\.ecommerceshoppingapp1\.MainActivity$\n?', ''
    $content = $content -replace '(?m)^import com\.example\.ecommerceshoppingapp1\.HomepageActivity$\n?', ''

    foreach ($key in $imports.Keys) {
        $importStmt = "import " + $imports[$key]
        if ($content -match "\b$key\b" -and $content -notmatch [regex]::Escape($importStmt)) {
            $content = $content -replace '(?m)^package\s+.*$', "$0`n$importStmt"
        }
    }

    Set-Content -Path $filePath -Value $content -Encoding UTF8
}
