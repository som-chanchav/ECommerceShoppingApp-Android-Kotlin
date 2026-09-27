import os
import re

base = r"E:\learn android\ECommerceShoppingApp1\app\src\main\java\com\example\ecommerceshoppingapp1"

packages = {
    "model": "package com.example.ecommerceshoppingapp1.model\n",
    "adapter": "package com.example.ecommerceshoppingapp1.adapter\n",
    "ui": "package com.example.ecommerceshoppingapp1.ui\n",
    "database": "package com.example.ecommerceshoppingapp1.database\n",
}

imports_map = {
    "CartAdapter.kt": ["com.example.ecommerceshoppingapp1.R", "com.example.ecommerceshoppingapp1.model.CartItem"],
    "CategoryAdapter.kt": ["com.example.ecommerceshoppingapp1.R", "com.example.ecommerceshoppingapp1.model.Category"],
    "OnboardingAdapter.kt": ["com.example.ecommerceshoppingapp1.R", "com.example.ecommerceshoppingapp1.model.OnboardingItem"],
    "ProductAdapter.kt": ["com.example.ecommerceshoppingapp1.R", "com.example.ecommerceshoppingapp1.model.Product"],
    
    "CartFragment.kt": ["com.example.ecommerceshoppingapp1.R", "com.example.ecommerceshoppingapp1.database.DatabaseHelper", "com.example.ecommerceshoppingapp1.model.CartItem", "com.example.ecommerceshoppingapp1.adapter.CartAdapter"],
    "DetailFragment.kt": ["com.example.ecommerceshoppingapp1.R", "com.example.ecommerceshoppingapp1.database.DatabaseHelper", "com.example.ecommerceshoppingapp1.model.Product", "com.example.ecommerceshoppingapp1.model.Category"],
    "FavoriteFragment.kt": ["com.example.ecommerceshoppingapp1.R"],
    "HomeFragment.kt": ["com.example.ecommerceshoppingapp1.R", "com.example.ecommerceshoppingapp1.database.DatabaseHelper", "com.example.ecommerceshoppingapp1.model.Category", "com.example.ecommerceshoppingapp1.model.Product", "com.example.ecommerceshoppingapp1.adapter.CategoryAdapter", "com.example.ecommerceshoppingapp1.adapter.ProductAdapter"],
    "HomepageActivity.kt": ["com.example.ecommerceshoppingapp1.R"],
    "LoginFragment.kt": ["com.example.ecommerceshoppingapp1.R", "com.example.ecommerceshoppingapp1.database.DatabaseHelper", "com.example.ecommerceshoppingapp1.ui.HomepageActivity"],
    "MainActivity.kt": ["com.example.ecommerceshoppingapp1.R"],
    "OnboardingFragment.kt": ["com.example.ecommerceshoppingapp1.R", "com.example.ecommerceshoppingapp1.model.OnboardingItem", "com.example.ecommerceshoppingapp1.adapter.OnboardingAdapter"],
    "ProfileFragment.kt": ["com.example.ecommerceshoppingapp1.R"],
    "RegisterFragment.kt": ["com.example.ecommerceshoppingapp1.R", "com.example.ecommerceshoppingapp1.database.DatabaseHelper"],
    "SecondFragment.kt": ["com.example.ecommerceshoppingapp1.R"],
}

for root, _, files in os.walk(base):
    for f in files:
        if f.endswith('.kt'):
            path = os.path.join(root, f)
            folder = os.path.basename(root)
            
            if folder not in packages:
                continue
                
            with open(path, 'r', encoding='utf-8') as file:
                content = file.read()
            
            # Remove existing package lines
            content = re.sub(r'^package\s+.*$\n?', '', content, flags=re.MULTILINE)
            
            # Remove all com.example imports (to clean up the mess) except databinding
            lines = content.split('\n')
            clean_lines = []
            for line in lines:
                if line.startswith('import com.example.ecommerceshoppingapp1.') and 'databinding' not in line:
                    continue
                clean_lines.append(line)
                
            # Now build the new file
            new_content = packages[folder] + "\n"
            
            # Add required imports
            if f in imports_map:
                for imp in imports_map[f]:
                    new_content += f"import {imp}\n"
            new_content += "\n"
            
            new_content += '\n'.join(clean_lines).strip() + "\n"
            
            with open(path, 'w', encoding='utf-8') as file:
                file.write(new_content)

print("Done fixing everything.")
