import os
import shutil

base_dir = r"E:\learn android\ECommerceShoppingApp1\app\src\main\java\com\example\ecommerceshoppingapp1"

files_to_move = {
    # Models
    "Category.kt": "model",
    "Product.kt": "model",
    r"cart\CartItem.kt": "model",
    r"OnboardingAdapter\OnboardingItem.kt": "model",
    
    # Adapters
    "CategoryAdapter.kt": "adapter",
    "ProductAdapter.kt": "adapter",
    r"cart\CartAdapter.kt": "adapter",
    r"OnboardingAdapter\OnboardingAdapter.kt": "adapter",
    
    # UI
    "MainActivity.kt": "ui",
    "HomepageActivity.kt": "ui",
    "HomeFragment.kt": "ui",
    "FavoriteFragment.kt": "ui",
    "ProfileFragment.kt": "ui",
    "SecondFragment.kt": "ui",
    r"cart\CartFragment.kt": "ui",
    r"cart\DetailFragment.kt": "ui",
    r"loginregister\LoginFragment.kt": "ui",
    r"loginregister\RegisterFragment.kt": "ui",
    r"OnboardingAdapter\OnboardingFragment.kt": "ui",
    
    # Database
    r"Database\DatabaseHelper.kt": "database"
}

# Create directories
for d in ["model", "adapter", "ui", "database"]:
    os.makedirs(os.path.join(base_dir, d), exist_ok=True)

# Move files and update package declaration
for old_path, new_folder in files_to_move.items():
    old_full_path = os.path.join(base_dir, old_path)
    filename = os.path.basename(old_path)
    new_full_path = os.path.join(base_dir, new_folder, filename)
    
    if os.path.exists(old_full_path):
        # Read content
        with open(old_full_path, "r", encoding="utf-8") as f:
            content = f.read()
            
        # Update package statement
        new_package = f"package com.example.ecommerceshoppingapp1.{new_folder}"
        # We find the first line starting with 'package' and replace it
        lines = content.split('\n')
        for i, line in enumerate(lines):
            if line.startswith("package "):
                lines[i] = new_package
                break
        
        # Write back to new path
        with open(new_full_path, "w", encoding="utf-8") as f:
            f.write('\n'.join(lines))
            
        # Delete old file
        os.remove(old_full_path)
        print(f"Moved {filename} to {new_folder} and updated package.")
    else:
        print(f"Warning: {old_full_path} not found.")

# Clean up empty directories
for d in ["cart", "loginregister", "OnboardingAdapter", "Database"]:
    dir_path = os.path.join(base_dir, d)
    if os.path.exists(dir_path):
        try:
            os.rmdir(dir_path)
            print(f"Removed empty directory {d}")
        except OSError:
            print(f"Could not remove directory {d} (might not be empty)")
