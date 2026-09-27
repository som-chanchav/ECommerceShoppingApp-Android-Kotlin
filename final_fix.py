import os
import re

base = r"E:\learn android\ECommerceShoppingApp1\app\src\main\java\com\example\ecommerceshoppingapp1"

dirs = {
    "model": "package com.example.ecommerceshoppingapp1.model\n\n",
    "adapter": "package com.example.ecommerceshoppingapp1.adapter\n\n",
    "ui": "package com.example.ecommerceshoppingapp1.ui\n\n",
    "database": "package com.example.ecommerceshoppingapp1.database\n\n"
}

for d, pkg in dirs.items():
    folder_path = os.path.join(base, d)
    if os.path.exists(folder_path):
        for f in os.listdir(folder_path):
            if f.endswith('.kt'):
                file_path = os.path.join(folder_path, f)
                with open(file_path, 'r', encoding='utf-8') as file:
                    content = file.read()
                
                content = re.sub(r'^package\b.*$', '', content, flags=re.MULTILINE)
                
                # Remove all specific com.example.ecommerceshoppingapp1 imports to start fresh
                content = re.sub(r'^import com\.example\.ecommerceshoppingapp1\.(model|adapter|ui|database)\..*$', '', content, flags=re.MULTILINE)
                
                new_imports = ""
                
                if "DatabaseHelper" in content:
                    new_imports += "import com.example.ecommerceshoppingapp1.database.DatabaseHelper\n"
                if "Category" in content and d != "model":
                    new_imports += "import com.example.ecommerceshoppingapp1.model.Category\n"
                if "Product" in content and d != "model":
                    new_imports += "import com.example.ecommerceshoppingapp1.model.Product\n"
                if "CartItem" in content and d != "model":
                    new_imports += "import com.example.ecommerceshoppingapp1.model.CartItem\n"
                if "OnboardingItem" in content and d != "model":
                    new_imports += "import com.example.ecommerceshoppingapp1.model.OnboardingItem\n"
                    
                if "CategoryAdapter" in content and d != "adapter":
                    new_imports += "import com.example.ecommerceshoppingapp1.adapter.CategoryAdapter\n"
                if "ProductAdapter" in content and d != "adapter":
                    new_imports += "import com.example.ecommerceshoppingapp1.adapter.ProductAdapter\n"
                if "CartAdapter" in content and d != "adapter":
                    new_imports += "import com.example.ecommerceshoppingapp1.adapter.CartAdapter\n"
                if "OnboardingAdapter" in content and d != "adapter":
                    new_imports += "import com.example.ecommerceshoppingapp1.adapter.OnboardingAdapter\n"
                    
                if "HomepageActivity" in content and d != "ui":
                    new_imports += "import com.example.ecommerceshoppingapp1.ui.HomepageActivity\n"
                
                content = content.strip()
                content = pkg + new_imports + "\n" + content + "\n"
                
                with open(file_path, 'w', encoding='utf-8') as file:
                    file.write(content)
