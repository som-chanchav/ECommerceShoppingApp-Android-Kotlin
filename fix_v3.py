import os

base = r"E:\learn android\ECommerceShoppingApp1\app\src\main\java\com\example\ecommerceshoppingapp1"

files_to_imports = {
    "HomeFragment.kt": [
        "import com.example.ecommerceshoppingapp1.R",
        "import com.example.ecommerceshoppingapp1.model.Category",
        "import com.example.ecommerceshoppingapp1.model.Product",
        "import com.example.ecommerceshoppingapp1.adapter.CategoryAdapter",
        "import com.example.ecommerceshoppingapp1.adapter.ProductAdapter",
        "import com.example.ecommerceshoppingapp1.database.DatabaseHelper"
    ],
    "DetailFragment.kt": [
        "import com.example.ecommerceshoppingapp1.database.DatabaseHelper",
        "import com.example.ecommerceshoppingapp1.model.Product",
        "import com.example.ecommerceshoppingapp1.model.Category"
    ],
    "CartFragment.kt": [
        "import com.example.ecommerceshoppingapp1.database.DatabaseHelper",
        "import com.example.ecommerceshoppingapp1.model.CartItem",
        "import com.example.ecommerceshoppingapp1.adapter.CartAdapter"
    ],
    "LoginFragment.kt": [
        "import com.example.ecommerceshoppingapp1.database.DatabaseHelper",
        "import com.example.ecommerceshoppingapp1.ui.HomepageActivity"
    ],
    "RegisterFragment.kt": [
        "import com.example.ecommerceshoppingapp1.database.DatabaseHelper"
    ],
    "OnboardingFragment.kt": [
        "import com.example.ecommerceshoppingapp1.model.OnboardingItem",
        "import com.example.ecommerceshoppingapp1.adapter.OnboardingAdapter"
    ],
    "CartAdapter.kt": ["import com.example.ecommerceshoppingapp1.model.CartItem"],
    "OnboardingAdapter.kt": ["import com.example.ecommerceshoppingapp1.model.OnboardingItem"],
    "CartItem.kt": [],
    "Category.kt": [],
    "OnboardingItem.kt": [],
    "Product.kt": ["import com.example.ecommerceshoppingapp1.model.Category"],
}

packages = {
    "model": "package com.example.ecommerceshoppingapp1.model",
    "adapter": "package com.example.ecommerceshoppingapp1.adapter",
    "ui": "package com.example.ecommerceshoppingapp1.ui",
    "database": "package com.example.ecommerceshoppingapp1.database",
}

for root, _, files in os.walk(base):
    for f in files:
        if f.endswith('.kt'):
            path = os.path.join(root, f)
            folder = os.path.basename(root)
            
            with open(path, 'r', encoding='utf-8') as file:
                lines = file.readlines()
                
            clean_lines = []
            for line in lines:
                if line.startswith("package "):
                    continue
                if line.startswith("import com.example.ecommerceshoppingapp1.") and "databinding" not in line and "R" not in line:
                    continue
                clean_lines.append(line)
                
            new_lines = []
            if folder in packages:
                new_lines.append(packages[folder] + "\n\n")
            
            if f in files_to_imports:
                for imp in files_to_imports[f]:
                    new_lines.append(imp + "\n")
            
            if f in ["DetailFragment.kt", "FavoriteFragment.kt", "HomeFragment.kt", "HomepageActivity.kt", "LoginFragment.kt", "MainActivity.kt", "OnboardingFragment.kt", "ProfileFragment.kt", "RegisterFragment.kt", "SecondFragment.kt", "CartFragment.kt", "CartAdapter.kt", "CategoryAdapter.kt", "OnboardingAdapter.kt", "ProductAdapter.kt"]:
                new_lines.append("import com.example.ecommerceshoppingapp1.R\n")
                
            if f == "HomeFragment.kt":
                new_lines.extend([
                    "import android.os.Bundle\n",
                    "import android.view.LayoutInflater\n",
                    "import android.view.View\n",
                    "import android.view.ViewGroup\n",
                    "import android.widget.Toast\n",
                    "import androidx.fragment.app.Fragment\n",
                    "import androidx.navigation.fragment.findNavController\n",
                    "import androidx.recyclerview.widget.LinearLayoutManager\n",
                    "import com.example.ecommerceshoppingapp1.databinding.FragmentHomeBinding\n",
                    "import android.text.SpannableString\n",
                    "import android.text.Spanned\n",
                    "import android.text.style.ForegroundColorSpan\n",
                    "import androidx.core.content.ContextCompat\n"
                ])
                
            new_lines.extend(clean_lines)
            
            with open(path, 'w', encoding='utf-8') as file:
                file.writelines(new_lines)

print("Done")
