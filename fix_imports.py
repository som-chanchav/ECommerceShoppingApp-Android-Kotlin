import os

base = r"E:\learn android\ECommerceShoppingApp1\app\src\main\java\com\example\ecommerceshoppingapp1"

replacements = {
    "import com.example.ecommerceshoppingapp1.Database.DatabaseHelper": "import com.example.ecommerceshoppingapp1.database.DatabaseHelper",
    "import com.example.ecommerceshoppingapp1.cart.CartAdapter": "import com.example.ecommerceshoppingapp1.adapter.CartAdapter",
    "import com.example.ecommerceshoppingapp1.cart.CartItem": "import com.example.ecommerceshoppingapp1.model.CartItem",
    "import com.example.ecommerceshoppingapp1.cart.DetailFragment": "import com.example.ecommerceshoppingapp1.ui.DetailFragment",
    "import com.example.ecommerceshoppingapp1.OnboardingAdapter.OnboardingAdapter": "import com.example.ecommerceshoppingapp1.adapter.OnboardingAdapter",
    "import com.example.ecommerceshoppingapp1.OnboardingAdapter.OnboardingItem": "import com.example.ecommerceshoppingapp1.model.OnboardingItem",
    "import com.example.ecommerceshoppingapp1.HomepageActivity": "import com.example.ecommerceshoppingapp1.ui.HomepageActivity",
    "import com.example.ecommerceshoppingapp1.MainActivity": "import com.example.ecommerceshoppingapp1.ui.MainActivity",
    "import com.example.ecommerceshoppingapp1.Product": "import com.example.ecommerceshoppingapp1.model.Product",
    "import com.example.ecommerceshoppingapp1.Category": "import com.example.ecommerceshoppingapp1.model.Category",
    "import com.example.ecommerceshoppingapp1.CategoryAdapter": "import com.example.ecommerceshoppingapp1.adapter.CategoryAdapter",
    "import com.example.ecommerceshoppingapp1.ProductAdapter": "import com.example.ecommerceshoppingapp1.adapter.ProductAdapter",
}

def add_import_if_needed(content, match_str, import_stmt):
    if match_str in content and import_stmt not in content:
        lines = content.split('\n')
        for i, line in enumerate(lines):
            if line.startswith('package '):
                lines.insert(i+1, f"\n{import_stmt}")
                break
        return '\n'.join(lines)
    return content

for root, _, files in os.walk(base):
    for f in files:
        if f.endswith('.kt'):
            path = os.path.join(root, f)
            with open(path, 'r', encoding='utf-8') as file:
                content = file.read()
            
            for old, new in replacements.items():
                content = content.replace(old, new)
            
            content = add_import_if_needed(content, 'Category', 'import com.example.ecommerceshoppingapp1.model.Category')
            content = add_import_if_needed(content, 'Product', 'import com.example.ecommerceshoppingapp1.model.Product')
            content = add_import_if_needed(content, 'CartItem', 'import com.example.ecommerceshoppingapp1.model.CartItem')
            content = add_import_if_needed(content, 'OnboardingItem', 'import com.example.ecommerceshoppingapp1.model.OnboardingItem')
            
            content = add_import_if_needed(content, 'CategoryAdapter', 'import com.example.ecommerceshoppingapp1.adapter.CategoryAdapter')
            content = add_import_if_needed(content, 'ProductAdapter', 'import com.example.ecommerceshoppingapp1.adapter.ProductAdapter')
            content = add_import_if_needed(content, 'CartAdapter', 'import com.example.ecommerceshoppingapp1.adapter.CartAdapter')
            content = add_import_if_needed(content, 'OnboardingAdapter', 'import com.example.ecommerceshoppingapp1.adapter.OnboardingAdapter')
            
            content = add_import_if_needed(content, 'DatabaseHelper', 'import com.example.ecommerceshoppingapp1.database.DatabaseHelper')
            content = add_import_if_needed(content, 'HomepageActivity', 'import com.example.ecommerceshoppingapp1.ui.HomepageActivity')
            content = add_import_if_needed(content, 'MainActivity', 'import com.example.ecommerceshoppingapp1.ui.MainActivity')
            
            content = add_import_if_needed(content, 'R.', 'import com.example.ecommerceshoppingapp1.R')

            with open(path, 'w', encoding='utf-8') as file:
                file.write(content)
