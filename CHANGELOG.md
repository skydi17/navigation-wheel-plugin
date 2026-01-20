# Changelog

All notable changes to this project will be documented in this file.

## [1.0.0] - 2024-10-08
### Added
- Used intellij-platform-plugin-template for gradle configuration.

### Fixed
- Fixed compatibility issues.

## [1.1.0] - 2024-10-14
### Added
- Refactored behavior so that the mouse cursor is no longer teleported to the center when closing a file.
- Improved compatibility with other versions of the IDE.
- Removed action with opening navigation wheel with code analysis.
- Clicking outside circle closing plugin.
- Changed appearance of wheel.

## [1.1.1] - 2025-12-15
### Fixed
- Fixed background transparency issue on Mac OS.
- Fixed opening file when clicking directly on FileButton.
- Fixed positioning of wheel on multi-monitor setups.
- Limited the number of files displayed on the wheel to a maximum of 10 to prevent overlapping and layout issues.
- Removed moving cursor to the center during closing file.

## [1.1.2] - 2025-12-28
### Fixed
- The wheel is no longer recreated when the close button is pressed.
- The close button is highlighted only when the mouse is directly over it.

## [1.1.3] - 2026-01-20
### Fixed
- Fixed an issue where the navigation wheel would not reopen or display correctly after pressing the close button on multi-monitor setups, especially when the IDE is on a secondary screen.
- Improved wheel resizing and positioning during refresh by explicitly setting window location and size to ensure it stays centered on the correct monitor.
- Resolved a race condition where the popup could be automatically closed by the IDE during file closing by disabling auto-cancellation and ensuring the popup is recreated if necessary.
- Enhanced active monitor detection by using KeyboardFocusManager to identify the correctly focused window, ensuring the wheel opens on the intended screen in multi-monitor environments.
- Added a delay using SwingUtilities.invokeLater when refreshing the wheel after closing a file to ensure all IDE focus and window events are processed.