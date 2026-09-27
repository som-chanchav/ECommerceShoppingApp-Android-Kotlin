import sys
import os
from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.dml.color import RGBColor
from pptx.enum.text import PP_ALIGN
from pptx.enum.shapes import MSO_SHAPE

def create_deck():
    prs = Presentation()
    # 16:9 widescreen layout
    prs.slide_width = Inches(13.333)
    prs.slide_height = Inches(7.5)

    # Color Palette (ACLEDA inspired: Deep Navy & Gold Accent)
    NAVY = RGBColor(11, 40, 83)       # #0B2853
    GOLD = RGBColor(212, 154, 36)     # #D49A24
    DARK_TEXT = RGBColor(33, 37, 41)  # #212529
    MUTED_TEXT = RGBColor(100, 116, 139) # #64748B
    WHITE = RGBColor(255, 255, 255)
    LIGHT_BG = RGBColor(248, 249, 250) # #F8F9FA
    CARD_BORDER = RGBColor(226, 232, 240)
    ACCENT_BLUE = RGBColor(37, 99, 235)

    blank_layout = prs.slide_layouts[6]

    def add_header(slide, title_text, category_text="E-COMMERCE SHOPPING APP PROJECT"):
        # Top banner
        header_shape = slide.shapes.add_shape(
            MSO_SHAPE.RECTANGLE, Inches(0), Inches(0), Inches(13.333), Inches(1.1)
        )
        header_shape.fill.solid()
        header_shape.fill.fore_color.rgb = NAVY
        header_shape.line.fill.background()

        # Gold bottom line for banner
        gold_line = slide.shapes.add_shape(
            MSO_SHAPE.RECTANGLE, Inches(0), Inches(1.06), Inches(13.333), Inches(0.06)
        )
        gold_line.fill.solid()
        gold_line.fill.fore_color.rgb = GOLD
        gold_line.line.fill.background()

        # Header Title Text
        txBox = slide.shapes.add_textbox(Inches(0.8), Inches(0.18), Inches(9.5), Inches(0.75))
        tf = txBox.text_frame
        tf.word_wrap = True
        tf.margin_left = tf.margin_top = tf.margin_right = tf.margin_bottom = 0
        
        p = tf.paragraphs[0]
        p.text = title_text
        p.font.name = "Segoe UI"
        p.font.size = Pt(24)
        p.font.bold = True
        p.font.color.rgb = WHITE

        # Category / University label right side
        txBox_right = slide.shapes.add_textbox(Inches(9.5), Inches(0.2), Inches(3.0), Inches(0.7))
        tf_r = txBox_right.text_frame
        tf_r.word_wrap = True
        p_r = tf_r.paragraphs[0]
        p_r.text = "ACLEDA UNIVERSITY"
        p_r.alignment = PP_ALIGN.RIGHT
        p_r.font.name = "Segoe UI"
        p_r.font.size = Pt(14)
        p_r.font.bold = True
        p_r.font.color.rgb = GOLD

        p_r2 = tf_r.add_paragraph()
        p_r2.text = category_text
        p_r2.alignment = PP_ALIGN.RIGHT
        p_r2.font.name = "Segoe UI"
        p_r2.font.size = Pt(10)
        p_r2.font.color.rgb = WHITE

    def add_card(slide, left, top, width, height, bg_color=LIGHT_BG, border_color=CARD_BORDER):
        card = slide.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, left, top, width, height)
        card.fill.solid()
        card.fill.fore_color.rgb = bg_color
        if border_color:
            card.line.color.rgb = border_color
            card.line.width = Pt(1.5)
        else:
            card.line.fill.background()
        return card

    # ==========================================
    # SLIDE 1: Title Slide
    # ==========================================
    s1 = prs.slides.add_slide(blank_layout)
    bg1 = s1.shapes.add_shape(MSO_SHAPE.RECTANGLE, Inches(0), Inches(0), Inches(13.333), Inches(7.5))
    bg1.fill.solid()
    bg1.fill.fore_color.rgb = NAVY
    bg1.line.fill.background()

    # Top gold accent line
    gold_top = s1.shapes.add_shape(MSO_SHAPE.RECTANGLE, Inches(0), Inches(0), Inches(13.333), Inches(0.15))
    gold_top.fill.solid()
    gold_top.fill.fore_color.rgb = GOLD
    gold_top.line.fill.background()

    # University Badge Box
    u_box = s1.shapes.add_textbox(Inches(1.0), Inches(1.2), Inches(11.333), Inches(0.6))
    u_tf = u_box.text_frame
    u_p = u_tf.paragraphs[0]
    u_p.text = "ACLEDA UNIVERSITY OF BUSINESS"
    u_p.font.name = "Segoe UI"
    u_p.font.size = Pt(16)
    u_p.font.bold = True
    u_p.font.color.rgb = GOLD

    # Title Text
    t_box = s1.shapes.add_textbox(Inches(1.0), Inches(2.0), Inches(11.333), Inches(2.2))
    t_tf = t_box.text_frame
    t_tf.word_wrap = True
    t_p = t_tf.paragraphs[0]
    t_p.text = "E-Commerce Shopping App"
    t_p.font.name = "Segoe UI"
    t_p.font.size = Pt(44)
    t_p.font.bold = True
    t_p.font.color.rgb = WHITE

    t_p2 = t_tf.add_paragraph()
    t_p2.text = "Android Application Development with Kotlin & SQLite Database"
    t_p2.font.name = "Segoe UI"
    t_p2.font.size = Pt(22)
    t_p2.font.color.rgb = RGBColor(203, 213, 225)
    t_p2.space_before = Pt(14)

    # Info card on bottom
    s1_card = add_card(s1, Inches(1.0), Inches(4.7), Inches(11.333), Inches(1.8), RGBColor(17, 48, 96), None)
    c_box = s1.shapes.add_textbox(Inches(1.3), Inches(4.9), Inches(10.7), Inches(1.4))
    c_tf = c_box.text_frame
    c_tf.word_wrap = True

    p = c_tf.paragraphs[0]
    p.text = "Project Outline & Presentation Report"
    p.font.name = "Segoe UI"
    p.font.size = Pt(16)
    p.font.bold = True
    p.font.color.rgb = GOLD

    p2 = c_tf.add_paragraph()
    p2.text = "• Package: com.example.ecommerceshoppingapp1\n• Platform: Android OS (Target SDK 37, Kotlin 2.x, ViewBinding)\n• Architecture: Modular Fragment UI, Custom Adapters, SQLite Local Storage"
    p2.font.name = "Segoe UI"
    p2.font.size = Pt(13)
    p2.font.color.rgb = WHITE
    p2.space_before = Pt(8)

    # ==========================================
    # SLIDE 2: Table of Contents / Outline
    # ==========================================
    s2 = prs.slides.add_slide(blank_layout)
    add_header(s2, "Presentation Contents / មាតិកា")

    toc_items = [
        ("1", "Introduction", "សេចក្តីផ្តើមអំពីគម្រោង និងបច្ចេកវិទ្យា"),
        ("2", "Benefits of study", "អត្ថប្រយោជន៍ចំពោះការសិក្សា និងអាជីវកម្ម"),
        ("3", "Scope of study", "វិសាលភាព និងមុខងារសំខាន់ៗរបស់ App"),
        ("4", "Process (Figure / Flow)", "ដំណើរការប្រព័ន្ធ ស្ថាបត្យកម្ម និង Database Schema"),
        ("5", "Questions", "សំណួរស្រាវជ្រាវ និងការវិភាគបច្ចេកទេស"),
        ("6", "Summary", "សេចក្តីសន្និដ្ឋាននៃលទ្ធផលគម្រោង"),
        ("7", "References", "ឯកសារយោង និងប្រភពបច្ចេកវិទ្យា")
    ]

    # Two column cards layout for TOC
    for idx, (num, en_title, kh_title) in enumerate(toc_items):
        col = 0 if idx < 4 else 1
        row = idx if idx < 4 else idx - 4
        
        left = Inches(1.0) if col == 0 else Inches(7.0)
        top = Inches(1.5 + row * 1.3)
        width = Inches(5.3)
        height = Inches(1.15)

        card = add_card(s2, left, top, width, height, WHITE, RGBColor(226, 232, 240))
        
        # Number badge
        badge = s2.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, left + Inches(0.15), top + Inches(0.2), Inches(0.75), Inches(0.75))
        badge.fill.solid()
        badge.fill.fore_color.rgb = NAVY
        badge.line.fill.background()
        
        b_tf = badge.text_frame
        b_p = b_tf.paragraphs[0]
        b_p.text = num
        b_p.font.name = "Segoe UI"
        b_p.font.size = Pt(20)
        b_p.font.bold = True
        b_p.font.color.rgb = GOLD
        b_p.alignment = PP_ALIGN.CENTER

        # Text box
        t_box = s2.shapes.add_textbox(left + Inches(1.05), top + Inches(0.15), Inches(4.1), Inches(0.85))
        t_tf = t_box.text_frame
        t_tf.word_wrap = True
        
        p1 = t_tf.paragraphs[0]
        p1.text = f"{num}. {en_title}"
        p1.font.name = "Segoe UI"
        p1.font.size = Pt(15)
        p1.font.bold = True
        p1.font.color.rgb = NAVY

        p2 = t_tf.add_paragraph()
        p2.text = kh_title
        p2.font.name = "Segoe UI"
        p2.font.size = Pt(12)
        p2.font.color.rgb = MUTED_TEXT

    # ==========================================
    # SLIDE 3: 1. Introduction
    # ==========================================
    s3 = prs.slides.add_slide(blank_layout)
    add_header(s3, "1. Introduction / សេចក្តីផ្តើម")

    # Card 1: Background & Vision
    add_card(s3, Inches(0.8), Inches(1.5), Inches(5.6), Inches(5.4), WHITE, CARD_BORDER)
    c1_box = s3.shapes.add_textbox(Inches(1.1), Inches(1.7), Inches(5.0), Inches(5.0))
    c1_tf = c1_box.text_frame
    c1_tf.word_wrap = True

    p = c1_tf.paragraphs[0]
    p.text = "📌 Background & Purpose (សាវតា និងគោលបំណង)"
    p.font.name = "Segoe UI"
    p.font.size = Pt(16)
    p.font.bold = True
    p.font.color.rgb = NAVY

    items_intro1 = [
        "E-Commerce Growth: ការរីកចម្រើនយ៉ាងឆាប់រហ័សនៃការទិញទំនិញតាមរយៈទូរស័ព្ទដៃ (M-Commerce)។",
        "Seamless Shopping Experience: បង្កើតកម្មវិធីដែលផ្តល់ភាពងាយស្រួល ក្នុងការរុករកទំនិញ មើលព័ត៌មានលម្អិត និងកុម្ម៉ង់ទិញ។",
        "Offline/Local Resilience: ការរក្សាទុកទិន្នន័យ (User, Products, Cart, Orders) ក្នុង Local SQLite Database ដែលដំណើរការលឿន និងមានសុវត្ថិភាព។",
        "Modern Mobile UX: រចនាបទ Material Design 3 ស្រស់ស្អាត ទំនើប ងាយយល់ និងងាយស្រួលបញ្ជា។"
    ]
    for it in items_intro1:
        p = c1_tf.add_paragraph()
        p.text = "• " + it
        p.font.name = "Segoe UI"
        p.font.size = Pt(12)
        p.font.color.rgb = DARK_TEXT
        p.space_before = Pt(10)

    # Card 2: Core Technologies
    add_card(s3, Inches(6.8), Inches(1.5), Inches(5.7), Inches(5.4), WHITE, CARD_BORDER)
    c2_box = s3.shapes.add_textbox(Inches(7.1), Inches(1.7), Inches(5.1), Inches(5.0))
    c2_tf = c2_box.text_frame
    c2_tf.word_wrap = True

    p = c2_tf.paragraphs[0]
    p.text = "🛠️ Technology Stack (បច្ចេកវិទ្យាស្នូល)"
    p.font.name = "Segoe UI"
    p.font.size = Pt(16)
    p.font.bold = True
    p.font.color.rgb = NAVY

    tech_items = [
        ("Language", "Kotlin 2.x (Modern, concise, type-safe)"),
        ("Platform", "Android SDK (Min SDK 24, Target SDK 37)"),
        ("Architecture", "Single Activity + Fragment Navigation Architecture"),
        ("UI & Binding", "Android ViewBinding & Material Design 3"),
        ("Local Database", "SQLite Database (via SQLiteOpenHelper)"),
        ("Components", "RecyclerView Adapters, ViewPager2, CardView, BottomNavigationView")
    ]
    for label, val in tech_items:
        p = c2_tf.add_paragraph()
        p.text = f"• {label}: "
        p.font.bold = True
        p.font.name = "Segoe UI"
        p.font.size = Pt(12)
        p.font.color.rgb = ACCENT_BLUE
        
        # Add value text
        p.text += val
        p.font.bold = False
        p.font.color.rgb = DARK_TEXT
        p.space_before = Pt(10)

    # ==========================================
    # SLIDE 4: 2. Benefits of study
    # ==========================================
    s4 = prs.slides.add_slide(blank_layout)
    add_header(s4, "2. Benefits of study / អត្ថប្រយោជន៍នៃការសិក្សា")

    # Column 1: Academic & Developer Benefits
    add_card(s4, Inches(0.8), Inches(1.5), Inches(5.6), Inches(5.4), WHITE, CARD_BORDER)
    b1_box = s4.shapes.add_textbox(Inches(1.1), Inches(1.7), Inches(5.0), Inches(5.0))
    b1_tf = b1_box.text_frame
    b1_tf.word_wrap = True

    p = b1_tf.paragraphs[0]
    p.text = "🎓 Academic & Technical Benefits (ផ្នែកសិក្សា/បច្ចេកទេស)"
    p.font.name = "Segoe UI"
    p.font.size = Pt(15)
    p.font.bold = True
    p.font.color.rgb = NAVY

    b_dev = [
        "ស្ទាត់ជំនាញក្នុងការសរសេរ Android App ដោយប្រើភាសា Kotlin តាមស្តង់ដារ Clean Code។",
        "ការអនុវត្តផ្ទាល់ជាមួយ SQLite Database ដូចជា Table Creation, CRUD Operations (Create, Read, Update, Delete)។",
        "ចេះគ្រប់គ្រង Fragment Lifecycle និងការបញ្ជូនទិន្នន័យ (Arguments/Bundles) រវាង Screen ផ្សេងៗ។",
        "ពង្រឹងជំនាញក្នុងការបង្កើត Custom RecyclerView Adapters សម្រាប់បង្ហាញទំនិញ, ប្រភេទ និងកន្ត្រកទំនិញ។"
    ]
    for it in b_dev:
        p = b1_tf.add_paragraph()
        p.text = "✔ " + it
        p.font.name = "Segoe UI"
        p.font.size = Pt(12)
        p.font.color.rgb = DARK_TEXT
        p.space_before = Pt(12)

    # Column 2: User & Business Benefits
    add_card(s4, Inches(6.8), Inches(1.5), Inches(5.7), Inches(5.4), WHITE, CARD_BORDER)
    b2_box = s4.shapes.add_textbox(Inches(7.1), Inches(1.7), Inches(5.1), Inches(5.0))
    b2_tf = b2_box.text_frame
    b2_tf.word_wrap = True

    p = b2_tf.paragraphs[0]
    p.text = "💼 User & Business Value (សម្រាប់អ្នកប្រើប្រាស់/អាជីវកម្ម)"
    p.font.name = "Segoe UI"
    p.font.size = Pt(15)
    p.font.bold = True
    p.font.color.rgb = NAVY

    b_biz = [
        "ភាពងាយស្រួលដល់អតិថិជន (Convenience): អាចមើលទំនិញ, ជ្រើសរើស Size/Color និងកុម្ម៉ង់ទិញបានយ៉ាងលឿនរហ័ស។",
        "ប្រព័ន្ធគ្រប់គ្រងការទិញពេញលេញ (Complete Ordering Flow): មានដំណាក់កាល Checkout ច្បាស់លាស់ (Address -> Payment -> Summary)។",
        "ការបង្កើនការលក់ (Promotions & Favorites): មានប្រព័ន្ធបង្ហាញទំនិញបញ្ចុះតម្លៃ (Discount) និងទំនិញពេញនិយម (Popular Products)។",
        "Cost-Effective & Scalable: ងាយស្រួលពង្រីកទៅភ្ជាប់ជាមួយ Cloud Server / REST API នៅជំហានបន្ទាប់។"
    ]
    for it in b_biz:
        p = b2_tf.add_paragraph()
        p.text = "✔ " + it
        p.font.name = "Segoe UI"
        p.font.size = Pt(12)
        p.font.color.rgb = DARK_TEXT
        p.space_before = Pt(12)

    # ==========================================
    # SLIDE 5: 3. Scope of study
    # ==========================================
    s5 = prs.slides.add_slide(blank_layout)
    add_header(s5, "3. Scope of study / វិសាលភាពនៃការសិក្សា")

    scopes = [
        ("1. Auth & Onboarding", "• ViewPager2 Onboarding\n• Login / Register forms\n• Guest Mode login option\n• User session management", Inches(0.8), Inches(1.5)),
        ("2. Catalog & Discovery", "• Promo Banner Carousel\n• Category Tabs Filter\n• Popular & All Products\n• Real-time Keyword Search", Inches(4.8), Inches(1.5)),
        ("3. Product Customization", "• Detailed description & rating\n• Size selector (S, M, L, XL)\n• Color selector chips\n• Add to Cart / Favorite toggle", Inches(8.8), Inches(1.5)),
        ("4. Cart Management", "• Real-time Quantity +/- \n• Item deletion / Clear cart\n• Dynamic Price & Fee calculation\n• Total Amount computation", Inches(0.8), Inches(4.3)),
        ("5. Multi-step Checkout", "• Step 1: Address Details\n• Step 2: Payment (Card/Cash)\n• Step 3: Order Confirmation\n• Order success dialog", Inches(4.8), Inches(4.3)),
        ("6. Local SQLite Storage", "• Table 'users' (Account data)\n• Table 'products' (Catalog)\n• Table 'cart_items' (Cart state)\n• Table 'orders' (Order history)", Inches(8.8), Inches(4.3)),
    ]

    for title, desc, left, top in scopes:
        card = add_card(s5, left, top, Inches(3.7), Inches(2.6), WHITE, CARD_BORDER)
        t_box = s5.shapes.add_textbox(left + Inches(0.15), top + Inches(0.15), Inches(3.4), Inches(2.3))
        t_tf = t_box.text_frame
        t_tf.word_wrap = True

        p = t_tf.paragraphs[0]
        p.text = title
        p.font.name = "Segoe UI"
        p.font.size = Pt(14)
        p.font.bold = True
        p.font.color.rgb = NAVY

        p2 = t_tf.add_paragraph()
        p2.text = desc
        p2.font.name = "Segoe UI"
        p2.font.size = Pt(11)
        p2.font.color.rgb = DARK_TEXT
        p2.space_before = Pt(6)

    # ==========================================
    # SLIDE 6: 4. Process (Show the figure - App Flowchart)
    # ==========================================
    s6 = prs.slides.add_slide(blank_layout)
    add_header(s6, "4. Process (System Workflow & Figure)")

    steps_flow = [
        ("Step 1", "Splash Screen", "Launch app & check user state"),
        ("Step 2", "Onboarding / Auth", "Slide tutorial -> Login / Register"),
        ("Step 3", "Home Dashboard", "Browse Banners, Categories, Search"),
        ("Step 4", "Product Detail", "Select Size/Color, Add to Cart"),
        ("Step 5", "Shopping Cart", "Review Items, Adjust Qty, Total"),
        ("Step 6", "Checkout Flow", "Address -> Payment -> Confirm Order"),
        ("Step 7", "SQLite Database", "Save to orders table & finalize")
    ]

    # Draw horizontal / connected flow
    for idx, (step_num, title, sub) in enumerate(steps_flow):
        col = idx % 4
        row = idx // 4
        
        left = Inches(0.8 + col * 3.0)
        top = Inches(1.6 + row * 2.6)
        width = Inches(2.7)
        height = Inches(2.2)

        card = add_card(s6, left, top, width, height, WHITE, CARD_BORDER)

        # Step header badge
        badge = s6.shapes.add_shape(MSO_SHAPE.RECTANGLE, left, top, width, Inches(0.45))
        badge.fill.solid()
        badge.fill.fore_color.rgb = NAVY if row == 0 else ACCENT_BLUE
        badge.line.fill.background()

        b_tf = badge.text_frame
        b_p = b_tf.paragraphs[0]
        b_p.text = step_num
        b_p.font.name = "Segoe UI"
        b_p.font.size = Pt(11)
        b_p.font.bold = True
        b_p.font.color.rgb = GOLD
        b_p.alignment = PP_ALIGN.CENTER

        # Body text
        t_box = s6.shapes.add_textbox(left + Inches(0.15), top + Inches(0.55), Inches(2.4), Inches(1.5))
        t_tf = t_box.text_frame
        t_tf.word_wrap = True

        p1 = t_tf.paragraphs[0]
        p1.text = title
        p1.font.name = "Segoe UI"
        p1.font.size = Pt(13)
        p1.font.bold = True
        p1.font.color.rgb = NAVY
        p1.alignment = PP_ALIGN.CENTER

        p2 = t_tf.add_paragraph()
        p2.text = sub
        p2.font.name = "Segoe UI"
        p2.font.size = Pt(10.5)
        p2.font.color.rgb = MUTED_TEXT
        p2.alignment = PP_ALIGN.CENTER
        p2.space_before = Pt(6)

    # Database Summary Callout
    db_card = add_card(s6, Inches(9.8), Inches(4.2), Inches(2.7), Inches(2.2), RGBColor(238, 242, 255), ACCENT_BLUE)
    db_box = s6.shapes.add_textbox(Inches(9.9), Inches(4.3), Inches(2.5), Inches(2.0))
    db_tf = db_box.text_frame
    db_tf.word_wrap = True
    p = db_tf.paragraphs[0]
    p.text = "💾 Database Schema"
    p.font.name = "Segoe UI"
    p.font.size = Pt(13)
    p.font.bold = True
    p.font.color.rgb = NAVY

    p2 = db_tf.add_paragraph()
    p2.text = "• users (Account info)\n• products (Catalog items)\n• cart_items (Cart state)\n• orders (Completed sales)"
    p2.font.name = "Segoe UI"
    p2.font.size = Pt(10)
    p2.font.color.rgb = DARK_TEXT
    p2.space_before = Pt(4)

    # ==========================================
    # SLIDE 7: 5. Questions
    # ==========================================
    s7 = prs.slides.add_slide(blank_layout)
    add_header(s7, "5. Questions / សំណួរស្រាវជ្រាវ និងពិភាក្សា")

    q_list = [
        ("Q1. Technical Architecture", "តើការប្រើប្រាស់ Single-Activity + Multiple Fragments មានផលវិជ្ជមានអ្វីខ្លះចំពោះ Memory Performance និង User Navigation?"),
        ("Q2. Data Persistence (SQLite vs Cloud)", "តើ SQLite Local Database ស័ក្តិសមសម្រាប់កម្រិតណា ហើយតើពេលណាដែលចាំបាច់ត្រូវប្តូរទៅប្រើ Cloud Database (REST API / Firebase)?"),
        ("Q3. Payment & Security Integration", "តើត្រូវបន្ថែមវិធានការសន្តិសុខបែបណា នៅពេលភ្ជាប់មុខងារទូទាត់ប្រាក់ពិតប្រាកដ (ដូចជា KHQR / Bakong / Stripe SDK)?"),
        ("Q4. User Experience & Scalability", "តើមុខងារអ្វីខ្លះដែលគួរពង្រីកបន្ថែមដើម្បីទាក់ទាញអតិថិជន (Push Notifications, Live Chat Support, Product Reviews) ?")
    ]

    for idx, (title, desc) in enumerate(q_list):
        top = Inches(1.5 + idx * 1.35)
        card = add_card(s7, Inches(0.8), top, Inches(11.7), Inches(1.2), WHITE, CARD_BORDER)

        # Question mark icon / box
        q_badge = s7.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(1.0), top + Inches(0.2), Inches(0.8), Inches(0.8))
        q_badge.fill.solid()
        q_badge.fill.fore_color.rgb = GOLD
        q_badge.line.fill.background()

        b_tf = q_badge.text_frame
        b_p = b_tf.paragraphs[0]
        b_p.text = f"Q{idx+1}"
        b_p.font.name = "Segoe UI"
        b_p.font.size = Pt(16)
        b_p.font.bold = True
        b_p.font.color.rgb = NAVY
        b_p.alignment = PP_ALIGN.CENTER

        # Question Text
        t_box = s7.shapes.add_textbox(Inches(2.0), top + Inches(0.12), Inches(10.3), Inches(0.95))
        t_tf = t_box.text_frame
        t_tf.word_wrap = True

        p1 = t_tf.paragraphs[0]
        p1.text = title
        p1.font.name = "Segoe UI"
        p1.font.size = Pt(13)
        p1.font.bold = True
        p1.font.color.rgb = NAVY

        p2 = t_tf.add_paragraph()
        p2.text = desc
        p2.font.name = "Segoe UI"
        p2.font.size = Pt(11.5)
        p2.font.color.rgb = DARK_TEXT
        p2.space_before = Pt(3)

    # ==========================================
    # SLIDE 8: 6. Summary
    # ==========================================
    s8 = prs.slides.add_slide(blank_layout)
    add_header(s8, "6. Summary / សេចក្តីសន្និដ្ឋាន")

    # Card 1: Key Achievements
    add_card(s8, Inches(0.8), Inches(1.5), Inches(5.6), Inches(5.4), WHITE, CARD_BORDER)
    s8_1_box = s8.shapes.add_textbox(Inches(1.1), Inches(1.7), Inches(5.0), Inches(5.0))
    s8_1_tf = s8_1_box.text_frame
    s8_1_tf.word_wrap = True

    p = s8_1_tf.paragraphs[0]
    p.text = "🎯 Key Achievements (សមិទ្ធផលសំខាន់ៗ)"
    p.font.name = "Segoe UI"
    p.font.size = Pt(15)
    p.font.bold = True
    p.font.color.rgb = NAVY

    achievements = [
        "បានអភិវឌ្ឍបញ្ចប់នូវ E-Commerce App ពេញលេញមួយដោយជោគជ័យតាមរចនាសម្ព័ន្ធ Kotlin & Android SDK។",
        "មុខងារស្នូលទាំងអស់ (Browsing, Category Filter, Product Detail, Cart, Multi-step Checkout, Orders) ដំណើរការបានរលូន។",
        "ប្រព័ន្ធមូលដ្ឋានទិន្នន័យ SQLite អាចផ្ទុក និងទាញយកទិន្នន័យបានត្រឹមត្រូវ និងរហ័ស។",
        "UI/UX ទំនើប ស្រស់ស្អាត ស្របតាមគោលការណ៍ Material Design 3។"
    ]
    for it in achievements:
        p = s8_1_tf.add_paragraph()
        p.text = "★ " + it
        p.font.name = "Segoe UI"
        p.font.size = Pt(12)
        p.font.color.rgb = DARK_TEXT
        p.space_before = Pt(12)

    # Card 2: Future Improvements
    add_card(s8, Inches(6.8), Inches(1.5), Inches(5.7), Inches(5.4), WHITE, CARD_BORDER)
    s8_2_box = s8.shapes.add_textbox(Inches(7.1), Inches(1.7), Inches(5.1), Inches(5.0))
    s8_2_tf = s8_2_box.text_frame
    s8_2_tf.word_wrap = True

    p = s8_2_tf.paragraphs[0]
    p.text = "🚀 Future Work & Roadmap (ទិសដៅអភិវឌ្ឍន៍បន្ត)"
    p.font.name = "Segoe UI"
    p.font.size = Pt(15)
    p.font.bold = True
    p.font.color.rgb = NAVY

    futures = [
        "Backend REST API Integration: តភ្ជាប់ជាមួយ Node.js / Laravel Server និង MySQL/PostgreSQL។",
        "Online Payment Gateway: ភ្ជាប់ប្រព័ន្ធទូទាត់ប្រាក់ KHQR / Bakong / Credit Card។",
        "Push Notifications: ជូនដំណឹងដល់អតិថិជនអំពីការបញ្ចុះតម្លៃ និងស្ថានភាពទំនិញតាម Firebase Cloud Messaging (FCM)។",
        "Admin Dashboard: បង្កើតផ្ទាំងគ្រប់គ្រងសម្រាប់ Admin ក្នុងការបន្ថែម/កែប្រែទំនិញ និងពិនិត្យមើលរបាយការណ៍លក់។"
    ]
    for it in futures:
        p = s8_2_tf.add_paragraph()
        p.text = "➔ " + it
        p.font.name = "Segoe UI"
        p.font.size = Pt(12)
        p.font.color.rgb = DARK_TEXT
        p.space_before = Pt(12)

    # ==========================================
    # SLIDE 9: 7. References
    # ==========================================
    s9 = prs.slides.add_slide(blank_layout)
    add_header(s9, "7. References / ឯកសារយោង")

    refs = [
        ("1. Official Android Developer Guides", "Google Developers - Android Architecture Components, UI Guides & Best Practices.\nURL: https://developer.android.com"),
        ("2. Kotlin Programming Language", "JetBrains - Modern Kotlin Programming Documentation & Standard Library Reference.\nURL: https://kotlinlang.org"),
        ("3. Material Design 3 Guidelines", "Google Design - Material 3 Components, Color Tokens & Typography.\nURL: https://m3.material.io"),
        ("4. SQLite Database Management in Android", "Android SDK SQLiteOpenHelper & Database Cursor API Guides.\nURL: https://developer.android.com/training/data-storage/sqlite"),
        ("5. Project Source Code & Assets", "ECommerceShoppingApp1 Package (com.example.ecommerceshoppingapp1) Local Codebase.")
    ]

    for idx, (title, details) in enumerate(refs):
        top = Inches(1.5 + idx * 1.08)
        card = add_card(s9, Inches(0.8), top, Inches(11.7), Inches(0.95), WHITE, CARD_BORDER)

        t_box = s9.shapes.add_textbox(Inches(1.1), top + Inches(0.1), Inches(11.1), Inches(0.75))
        t_tf = t_box.text_frame
        t_tf.word_wrap = True

        p1 = t_tf.paragraphs[0]
        p1.text = title
        p1.font.name = "Segoe UI"
        p1.font.size = Pt(13)
        p1.font.bold = True
        p1.font.color.rgb = NAVY

        p2 = t_tf.add_paragraph()
        p2.text = details
        p2.font.name = "Segoe UI"
        p2.font.size = Pt(10.5)
        p2.font.color.rgb = MUTED_TEXT
        p2.space_before = Pt(2)

    # ==========================================
    # SLIDE 10: Thank You / Q&A
    # ==========================================
    s10 = prs.slides.add_slide(blank_layout)
    bg10 = s10.shapes.add_shape(MSO_SHAPE.RECTANGLE, Inches(0), Inches(0), Inches(13.333), Inches(7.5))
    bg10.fill.solid()
    bg10.fill.fore_color.rgb = NAVY
    bg10.line.fill.background()

    # Top & Bottom gold accents
    g_top = s10.shapes.add_shape(MSO_SHAPE.RECTANGLE, Inches(0), Inches(0), Inches(13.333), Inches(0.15))
    g_top.fill.solid()
    g_top.fill.fore_color.rgb = GOLD
    g_top.line.fill.background()

    g_bot = s10.shapes.add_shape(MSO_SHAPE.RECTANGLE, Inches(0), Inches(7.35), Inches(13.333), Inches(0.15))
    g_bot.fill.solid()
    g_bot.fill.fore_color.rgb = GOLD
    g_bot.line.fill.background()

    th_box = s10.shapes.add_textbox(Inches(1.0), Inches(2.2), Inches(11.333), Inches(3.0))
    th_tf = th_box.text_frame
    th_tf.word_wrap = True

    p = th_tf.paragraphs[0]
    p.text = "THANK YOU!"
    p.alignment = PP_ALIGN.CENTER
    p.font.name = "Segoe UI"
    p.font.size = Pt(54)
    p.font.bold = True
    p.font.color.rgb = GOLD

    p2 = th_tf.add_paragraph()
    p2.text = "Any Questions & Feedback?"
    p2.alignment = PP_ALIGN.CENTER
    p2.font.name = "Segoe UI"
    p2.font.size = Pt(26)
    p2.font.color.rgb = WHITE
    p2.space_before = Pt(10)

    p3 = th_tf.add_paragraph()
    p3.text = "E-Commerce Shopping App • ACLEDA University of Business"
    p3.alignment = PP_ALIGN.CENTER
    p3.font.name = "Segoe UI"
    p3.font.size = Pt(14)
    p3.font.color.rgb = RGBColor(203, 213, 225)
    p3.space_before = Pt(20)

    output_path = os.path.join(os.getcwd(), "ECommerce_Shopping_App_Presentation.pptx")
    prs.save(output_path)
    print(f"Presentation saved successfully to: {output_path}")

if __name__ == "__main__":
    create_deck()
