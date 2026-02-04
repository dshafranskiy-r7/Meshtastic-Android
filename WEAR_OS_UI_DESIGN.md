# Meshtastic Wear OS - UI Flow

## Screen Structure

```
┌─────────────────────────────┐
│       Home Screen           │
│                             │
│  [Meshtastic Logo/Title]    │
│                             │
│   ┌───────────────────┐    │
│   │    Messages       │    │
│   └───────────────────┘    │
│                             │
│   ┌───────────────────┐    │
│   │     Nodes         │    │
│   └───────────────────┘    │
│                             │
│   "Standalone Mode"         │
│                             │
└─────────────────────────────┘
       ↓           ↓
       ↓           ↓
       ↓           └──────────┐
       ↓                      ↓
┌──────────────────┐   ┌─────────────────┐
│  Messages Screen │   │  Nodes Screen   │
│                  │   │                 │
│  ┌────────────┐  │   │ ┌─────────────┐ │
│  │ Contact 1  │  │   │ │ Node 1      │ │
│  │ Last msg.. │  │   │ │ 150m • 98% │ │
│  └────────────┘  │   │ └─────────────┘ │
│                  │   │                 │
│  ┌────────────┐  │   │ ┌─────────────┐ │
│  │ Contact 2  │  │   │ │ Node 2      │ │
│  │ Last msg.. │  │   │ │ 2.5km • 45%│ │
│  └────────────┘  │   │ └─────────────┘ │
│                  │   │                 │
│  ┌────────────┐  │   │ ┌─────────────┐ │
│  │ Contact 3  │  │   │ │ Node 3      │ │
│  │ Last msg.. │  │   │ │ 500m • 72% │ │
│  └────────────┘  │   │ └─────────────┘ │
│                  │   │                 │
└──────────────────┘   └─────────────────┘
       ↓                      
       ↓                      
┌──────────────────────────┐
│  Message Detail/Compose  │
│                          │
│  "Hello from watch!"     │
│                          │
│  ┌─────────────────────┐ │
│  │  [Send Button]      │ │
│  └─────────────────────┘ │
│                          │
│  Quick Replies:          │
│  • "OK"                  │
│  • "On my way"           │
│  • "Thanks"              │
│                          │
└──────────────────────────┘
```

## UI Components Used

### Home Screen
- **TimeText**: Shows current time at top of screen
- **ScalingLazyColumn**: For scrollable list of menu items
- **Chip**: Primary action buttons for Messages and Nodes
- **Vignette**: Fade effect at edges for AMOLED screens
- **Text**: Title and status text

### Messages Screen
- **ScalingLazyColumn**: For scrolling message list
- **Card/Chip**: Each message preview
- **Text**: Contact name and message preview
- **Icon**: Message status indicators

### Nodes Screen
- **ScalingLazyColumn**: For scrolling node list
- **ListItem/Chip**: Each node in list
- **Text**: Node name, distance, signal strength
- **Icon**: Status indicators (online, battery, etc.)

### Navigation
- **SwipeDismissableNavHost**: Enables swipe-back gesture
- **NavController**: Manages navigation state
- **Compose Navigation**: Type-safe navigation

## Wear OS Specific Features

### Input Methods
1. **Touch**: Primary input method
2. **Rotary Input**: Scroll through lists (Galaxy Watch bezel)
3. **Voice**: For message composition (future)
4. **Quick Replies**: Pre-defined message templates

### Screen Adaptations
- **Round Display**: Primary design target (e.g., Galaxy Watch)
- **Square Display**: Also supported (e.g., some older watches)
- **Size Variants**: 
  - Small: ~280px diameter
  - Medium: ~390px diameter (Galaxy Watch 4)
  - Large: ~450px diameter

### Battery Optimization
- **AMOLED-friendly**: Dark theme with vignette effect
- **Minimal animations**: Smooth but power-efficient
- **Background restrictions**: Proper foreground service handling
- **Efficient updates**: Only when screen is on

## User Experience Flow

### Initial Setup (Standalone Mode)
1. Open Meshtastic on watch
2. Tap "Pair Device" (future feature)
3. Select Meshtastic radio from Bluetooth list
4. Grant permissions (Bluetooth, Location, Notifications)
5. Connect and start using

### Daily Usage
1. Raise wrist to wake watch
2. Launch Meshtastic from app drawer or tile
3. See home screen with quick access buttons
4. Tap Messages to view/send messages
5. Tap Nodes to see network status
6. Swipe right to go back
7. Use rotary dial to scroll through lists

### Message Workflow
1. Open Messages
2. Scroll through conversations
3. Tap conversation to open
4. View message history
5. Compose reply (voice or quick reply)
6. Send message
7. See delivery confirmation

### Node Monitoring
1. Open Nodes
2. See list of all nodes in mesh
3. View distance and signal strength
4. See battery levels
5. Monitor network health
6. Tap node for details (future)

## Design Principles

### Glanceable
- Show most important info at a glance
- Large, clear text
- High contrast colors
- Minimal clutter

### Touch-friendly
- Large tap targets (at least 48dp)
- Adequate spacing between elements
- Chunky UI elements for thick fingers

### Efficient
- Quick access to key features
- Minimal navigation depth
- Fast loading times
- Battery conscious

### Accessible
- Support different screen sizes
- Work with rotary input
- Clear visual hierarchy
- Good contrast ratios

## Color Scheme (Wear Material Theme)

```
Primary: #6200EE (Material Purple)
On Primary: #FFFFFF
Background: #000000 (True black for AMOLED)
Surface: #121212 (Dark surface)
On Surface: #FFFFFF
Error: #CF6679
```

## Typography (Wear Material Theme)

```
Title1: 24sp - Screen titles
Title2: 20sp - Section headers
Body1: 16sp - Body text
Body2: 14sp - Secondary text
Caption1: 14sp - Captions
Caption2: 12sp - Small labels
Button: 15sp - Button text
```

## Future UI Enhancements

1. **Watch Face Complication**: Show mesh status on watch face
2. **Tiles**: Quick access tile with status info
3. **Voice Input**: Dictate messages
4. **Emojis**: Quick emoji picker
5. **Custom Themes**: User-selectable color schemes
6. **Animations**: Subtle transitions and loading states
7. **Gestures**: Additional gesture shortcuts
8. **Notifications**: Rich notifications with actions
