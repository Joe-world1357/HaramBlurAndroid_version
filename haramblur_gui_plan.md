# HaramBlur - GUI Design Plan & Specifications

## 🎨 Design Philosophy

### Core Principles
- **Islamic Aesthetics**: Clean, modest design with Islamic geometric patterns
- **Privacy First**: Visual emphasis on local processing and data security
- **Accessibility**: High contrast, readable fonts, and clear navigation
- **Emergency Focus**: CODE RED system prominently featured with urgent visual cues
- **Simplicity**: Intuitive interface that doesn't overwhelm users

### Color Palette
```
Primary Colors:
├── Islamic Green: #006B3C (Primary actions, active states)
├── Pure White: #FFFFFF (Backgrounds, cards)
├── Deep Black: #000000 (Text, icons)
├── Soft Gray: #F5F5F5 (Secondary backgrounds)
└── Light Gray: #E0E0E0 (Borders, dividers)

CODE RED Colors:
├── Alert Red: #D32F2F (Emergency actions, warnings)
├── Dark Red: #B71C1C (CODE RED backgrounds)
├── Light Red: #FFEBEE (CODE RED card backgrounds)
└── Emergency Orange: #FF5722 (Urgent notifications)

Status Colors:
├── Success Green: #4CAF50 (Protection active)
├── Warning Amber: #FFC107 (Needs attention)
├── Error Red: #F44336 (Issues, blocked content)
└── Info Blue: #2196F3 (Statistics, information)
```

### Typography
```
Font Family: Roboto (Primary) + Amiri (Arabic text)

Hierarchy:
├── Headline 1: 32sp, Bold (Main titles)
├── Headline 2: 24sp, Bold (Section headers)
├── Headline 3: 20sp, Medium (Card titles)
├── Body 1: 16sp, Regular (Main content)
├── Body 2: 14sp, Regular (Secondary content)
├── Caption: 12sp, Regular (Labels, timestamps)
└── Arabic Text: Amiri, 18sp (Islamic content)
```

## 📱 Screen Structure & Navigation

### Main Navigation (Bottom Navigation)
```
┌─────────────────────────────────────────────┐
│ [🏠 Home] [📊 Stats] [⚙️ Settings] [🚨 CODE RED] │
└─────────────────────────────────────────────┘
```

### Screen Hierarchy
```
App Structure:
├── 🏠 Home Screen (Dashboard)
│   ├── Protection Status Card
│   ├── Quick Stats Card
│   ├── Recent Activity Card
│   └── Islamic Quote of the Day
├── 📊 Statistics Screen
│   ├── Daily/Weekly/Monthly Views
│   ├── Content Detection Charts
│   ├── Website Blocking Stats
│   └── Usage Analytics
├── ⚙️ Settings Screen
│   ├── Detection Settings
│   ├── Blocking Preferences
│   ├── Islamic Reminders Config
│   ├── Privacy Settings
│   └── App Preferences
└── 🚨 CODE RED Screen
    ├── Emergency App Locking
    ├── Active Sessions Management
    ├── Quick Lock Presets
    └── Emergency Unlock
```

## 🏠 Home Screen Design

### Layout Structure
```
┌─────────────────────────────────────────┐
│            Top App Bar                  │
│  🌙 HaramBlur    [🔔] [👤] [⋮]         │
├─────────────────────────────────────────┤
│                                         │
│  ┌─── Protection Status Card ─────────┐ │
│  │ 🛡️ Protection: ACTIVE              │ │
│  │ ⚪ Toggle Switch                    │ │
│  │ 📊 2 detections today              │ │
│  │ ⏰ Active for 3h 24m               │ │
│  └─────────────────────────────────────┘ │
│                                         │
│  ┌─── CODE RED Alert ─────────────────┐ │
│  │ 🚨 Emergency App Lock               │ │
│  │ "Lock distracting apps instantly"   │ │
│  │ [ACTIVATE CODE RED] (Red Button)    │ │
│  └─────────────────────────────────────┘ │
│                                         │
│  ┌─── Today's Protection ─────────────┐ │
│  │ 📈 5 Content Blocked                │ │
│  │ 🚫 3 Websites Blocked               │ │
│  │ 📱 2 Apps Currently Locked          │ │
│  │ [View Details >]                    │ │
│  └─────────────────────────────────────┘ │
│                                         │
│  ┌─── Islamic Reminder ───────────────┐ │
│  │ 🕌 "Say to the believing men that   │ │
│  │     they should lower their gaze"   │ │
│  │                    - Quran 24:30    │ │
│  └─────────────────────────────────────┘ │
│                                         │
└─────────────────────────────────────────┘
```

### Protection Status Card States
```
🟢 ACTIVE STATE:
┌─────────────────────────────────────┐
│ 🛡️ Protection is ACTIVE            │
│ ●○○○○ [Toggle ON]                   │
│ Real-time monitoring enabled        │
│ Last detection: 2 minutes ago       │
└─────────────────────────────────────┘

🔴 INACTIVE STATE:
┌─────────────────────────────────────┐
│ ⚠️ Protection is OFF                │
│ ○●○○○ [Toggle OFF]                  │
│ Tap to enable protection            │
│ Your privacy is at risk             │
└─────────────────────────────────────┘

🟡 SETUP NEEDED:
┌─────────────────────────────────────┐
│ ⚙️ Setup Required                   │
│ Missing permissions:                │
│ • Screen recording access           │
│ • Accessibility service            │
│ [COMPLETE SETUP]                    │
└─────────────────────────────────────┘
```

## 🚨 CODE RED Screen Design

### Main CODE RED Interface
```
┌─────────────────────────────────────────┐
│         🚨 CODE RED EMERGENCY           │
│    "Lock apps when you need it most"   │
├─────────────────────────────────────────┤
│                                         │
│  ┌─── Quick Lock Presets ─────────────┐ │
│  │ 📱 Social Media (5 apps) [15 min]  │ │
│  │ 🎮 Gaming (3 apps) [1 hour]        │ │
│  │ 🎬 Entertainment (4 apps) [30 min] │ │
│  │ 💬 Messaging (6 apps) [2 hours]    │ │
│  └─────────────────────────────────────┘ │
│                                         │
│  ┌─── Custom Lock ────────────────────┐ │
│  │ Select Apps to Lock:                │ │
│  │ ☑️ Instagram    ☑️ TikTok          │ │
│  │ ☑️ YouTube      ☐ Twitter          │ │
│  │ ☐ Facebook     ☑️ Snapchat         │ │
│  │                                     │ │
│  │ Duration: [1 Hour ▼]               │ │
│  │ Password: [●●●●●●●●]               │ │
│  │ Reason: [Fighting urges...]         │ │
│  │                                     │ │
│  │ [🚨 ACTIVATE LOCK] (Red Button)     │ │
│  └─────────────────────────────────────┘ │
│                                         │
│  ┌─── Active Sessions ────────────────┐ │
│  │ 📱 2 apps locked                    │ │
│  │ ⏰ 1h 34m remaining                 │ │
│  │ [View Details] [Emergency Unlock]   │ │
│  └─────────────────────────────────────┘ │
│                                         │
└─────────────────────────────────────────┘
```

### CODE RED Confirmation Dialog
```
┌─────────────────────────────────────────┐
│             ⚠️ FINAL WARNING            │
├─────────────────────────────────────────┤
│                                         │
│ You are about to lock 3 apps:          │
│ • Instagram                             │
│ • TikTok                                │
│ • YouTube                               │
│                                         │
│ Duration: 2 hours                       │
│                                         │
│ ⚠️ This CANNOT be undone unless:        │
│ • Time expires (2:00:00)                │
│ • Correct password entered              │
│                                         │
│ Reason: "Avoiding temptation"           │
│                                         │
│ Are you absolutely sure?                │
│                                         │
│ [Cancel] [🚨 YES, ACTIVATE CODE RED]    │
│                                         │
└─────────────────────────────────────────┘
```

### Active CODE RED Session
```
┌─────────────────────────────────────────┐
│           🚨 CODE RED ACTIVE            │
├─────────────────────────────────────────┤
│                                         │
│  ┌─── Locked Apps ────────────────────┐ │
│  │ 📱 Instagram     ⏰ 1:23:45        │ │
│  │ 📱 TikTok        ⏰ 1:23:45        │ │
│  │ 📱 YouTube       ⏰ 1:23:45        │ │
│  │                                     │ │
│  │ Status: 🔴 LOCKED                   │ │
│  │ Started: 2:34 PM                    │ │
│  │ Ends: 4:34 PM                       │ │
│  │                                     │ │
│  │ Reason: "Avoiding temptation"       │ │
│  └─────────────────────────────────────┘ │
│                                         │
│  ┌─── Emergency Unlock ───────────────┐ │
│  │ Enter password to unlock early:     │ │
│  │ [Password Input Field]              │ │
│  │ [🔓 UNLOCK]                         │ │
│  │                                     │ │
│  │ ⚠️ Think carefully before unlocking │ │
│  └─────────────────────────────────────┘ │
│                                         │
└─────────────────────────────────────────┘
```

## 📊 Statistics Screen Design

### Statistics Dashboard
```
┌─────────────────────────────────────────┐
│              📊 Statistics              │
│        [Today] [Week] [Month]           │
├─────────────────────────────────────────┤
│                                         │
│  ┌─── Protection Summary ─────────────┐ │
│  │     Today's Protection              │ │
│  │                                     │ │
│  │  🛡️     🚫     📱     ⏰           │ │
│  │   5      3      2     7h           │ │
│  │Content Sites Apps  Active          │ │
│  │Blocked Block Lock  Time            │ │
│  └─────────────────────────────────────┘ │
│                                         │
│  ┌─── Weekly Chart ───────────────────┐ │
│  │     Content Detections              │ │
│  │ 10 ┤                            ●   │ │
│  │  8 ┤        ●                       │ │
│  │  6 ┤    ●       ●           ●       │ │
│  │  4 ┤●       ●       ●   ●           │ │
│  │  2 ┤                                │ │
│  │  0 └─┬─┬─┬─┬─┬─┬─┬─                │ │
│  │    M T W T F S S                   │ │
│  └─────────────────────────────────────┘ │
│                                         │
│  ┌─── Top Blocked Sites ──────────────┐ │
│  │ 1. instagram.com        (12 times) │ │
│  │ 2. tiktok.com           (8 times)  │ │
│  │ 3. youtube.com          (6 times)  │ │
│  │ 4. facebook.com         (4 times)  │ │
│  │ [View All >]                        │ │
│  └─────────────────────────────────────┘ │
│                                         │
└─────────────────────────────────────────┘
```

## ⚙️ Settings Screen Design

### Settings Menu Structure
```
┌─────────────────────────────────────────┐
│               ⚙️ Settings               │
├─────────────────────────────────────────┤
│                                         │
│  🛡️ Detection Settings                  │
│  ├── Content Types                      │
│  ├── Sensitivity Levels                 │
│  └── Blur Effects                       │
│                                         │
│  🚫 Blocking Preferences                │
│  ├── Website Categories                 │
│  ├── Custom Block Lists                 │
│  └── Blocking Actions                   │
│                                         │
│  🕌 Islamic Reminders                   │
│  ├── Reminder Types                     │
│  ├── Display Duration                   │
│  └── Language Settings                  │
│                                         │
│  🔒 Privacy & Security                  │
│  ├── Data Retention                     │
│  ├── Usage Logs                         │
│  └── Export Settings                    │
│                                         │
│  📱 App Preferences                     │
│  ├── Theme Settings                     │
│  ├── Notifications                      │
│  └── Performance                        │
│                                         │
│  ❓ Help & Support                      │
│  ├── Setup Guide                        │
│  ├── Troubleshooting                    │
│  └── Contact Support                    │
│                                         │
└─────────────────────────────────────────┘
```

### Detection Settings Detail
```
┌─────────────────────────────────────────┐
│          🛡️ Detection Settings          │
├─────────────────────────────────────────┤
│                                         │
│  Content Types to Detect:               │
│  ☑️ Female faces and figures            │
│  ☑️ NSFW/Adult content                  │
│  ☑️ Nudity detection                    │
│  ☐ Suggestive content                   │
│                                         │
│  Detection Sensitivity:                 │
│  Low ●────○────────── High              │
│           (Currently: Medium)           │
│                                         │
│  Blur Effects:                          │
│  ○ Gaussian Blur                        │
│  ○ Black Box                            │
│  ● Pixelated                            │
│  ○ Islamic Pattern                      │
│                                         │
│  Performance:                           │
│  Frame Rate: [3 FPS ▼]                 │
│  Processing Quality: [Medium ▼]         │
│                                         │
│  [Save Settings]                        │
│                                         │
└─────────────────────────────────────────┘
```

## 🎨 Visual Components

### Islamic Design Elements
```
Geometric Patterns:
├── 8-pointed star (Khatam) for decorative elements
├── Arabesque patterns for card borders
├── Calligraphy-inspired dividers
└── Subtle crescent moon icons

Islamic Colors:
├── Deep emerald green for primary actions
├── Gold accents for special elements  
├── Calming blue for informational content
└── Pure white for cleanliness and purity
```

### Card Design System
```
Standard Card:
┌─────────────────────────────────────┐
│ 📊 Card Title               [Icon] │
├─────────────────────────────────────┤
│                                     │
│ Card content goes here with         │
│ proper spacing and typography       │
│                                     │
│ [Action Button]                     │
└─────────────────────────────────────┘

Properties:
├── Corner Radius: 12dp
├── Elevation: 4dp
├── Padding: 16dp
├── Margin: 8dp
└── Background: White (#FFFFFF)
```

### Button Styles
```
Primary Button (Islamic Green):
┌─────────────────────┐
│    PRIMARY ACTION   │
└─────────────────────┘

CODE RED Button (Alert Red):
┌─────────────────────┐
│  🚨 EMERGENCY ACTION │
└─────────────────────┘

Secondary Button (Outlined):
┌─────────────────────┐
│   SECONDARY ACTION  │
└─────────────────────┘

Text Button (No background):
  TEXT ACTION
```

## 📐 Responsive Design

### Screen Size Adaptations
```
Phone Portrait (360dp+):
├── Single column layout
├── Full-width cards
├── Bottom navigation
└── Collapsing toolbar

Phone Landscape:
├── Two-column grid where possible
├── Compact navigation
├── Reduced margins
└── Optimized for thumb reach

Tablet (600dp+):
├── Two or three-column layout
├── Side navigation drawer
├── Larger cards with more content
└── Enhanced typography scale
```

### Accessibility Features
```
Visual Accessibility:
├── High contrast color combinations
├── Minimum 44dp touch targets
├── Clear visual hierarchy
├── Support for system font scaling

Motor Accessibility:
├── Large touch areas
├── Easy thumb reach on phones
├── Swipe gestures where appropriate
└── Voice control support

Cognitive Accessibility:
├── Clear, simple language
├── Consistent navigation patterns
├── Progress indicators for long tasks
└── Confirmation dialogs for critical actions
```

## 🌙 Dark Theme Adaptation

### Dark Mode Color Palette
```
Dark Theme Colors:
├── Background: #121212 (Almost black)
├── Surface: #1E1E1E (Dark gray)
├── Primary: #4CAF50 (Lighter green)
├── On Background: #FFFFFF (White text)
├── On Surface: #E0E0E0 (Light gray text)
└── CODE RED: #FF6B6B (Softer red for dark mode)
```

### Dark Mode Examples
```
Dark Home Screen:
┌─────────────────────────────────────────┐
│🌙 HaramBlur                    [🔔][👤]│
├─────────────────────────────────────────┤
│ Background: #121212                     │
│                                         │
│  ┌─── Protection Status (Dark) ──────┐  │
│  │ 🛡️ Protection: ACTIVE             │  │
│  │ Background: #1E1E1E                │  │
│  │ Text: #FFFFFF                      │  │
│  └─────────────────────────────────────┘  │
│                                         │
└─────────────────────────────────────────┘
```

## 🔄 Animation & Transitions

### Micro-Interactions
```
Loading States:
├── Shimmer effect for loading cards
├── Pulse animation for active protection
├── Smooth toggle switches
└── Ripple effects on buttons

Transitions:
├── Shared element transitions between screens
├── Slide animations for navigation
├── Fade in/out for overlays
└── Spring animations for CODE RED activation

Feedback:
├── Haptic feedback for important actions
├── Success animations for completed tasks
├── Error shake animation for invalid inputs
└── Progress indicators for long operations
```

This comprehensive GUI design plan provides the foundation for creating a beautiful, functional, and Islamic-compliant user interface for HaramBlur. The design emphasizes privacy, emergency features, and spiritual guidance while maintaining modern Android design standards.