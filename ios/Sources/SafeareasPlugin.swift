import SwiftRs
import Tauri
import UIKit
import WebKit

let MAGIC_NUMBER = 20030810;

extension UIColor {
    convenience init?(colorString: String) {
        if colorString.hasPrefix("#") {
            // HEX color
            self.init(hex: colorString)
        } else if colorString.starts(with: "rgb") {
            // RGB or RGBA color
            self.init(rgbString: colorString)
        } else if let namedColor = UIColor.perform(Selector(colorString.lowercased()))?.takeUnretainedValue() as? UIColor {
            // Named color (e.g., "red", "blue")
            self.init(cgColor: namedColor.cgColor)
        } else {
            return nil
        }
    }
    
    convenience init?(hex: String) {
        let r, g, b, a: CGFloat
        
        if hex.hasPrefix("#") {
            let start = hex.index(hex.startIndex, offsetBy: 1)
            let hexColor = String(hex[start...])
            
            if hexColor.count == 6 || hexColor.count == 8 {
                let scanner = Scanner(string: hexColor)
                var hexNumber: UInt64 = 0
                
                if scanner.scanHexInt64(&hexNumber) {
                    r = CGFloat((hexNumber & 0xff0000) >> 16) / 255
                    g = CGFloat((hexNumber & 0x00ff00) >> 8) / 255
                    b = CGFloat(hexNumber & 0x0000ff) / 255
                    a = hexColor.count == 8 ? CGFloat((hexNumber & 0xff000000) >> 24) / 255 : 1.0
                    
                    self.init(red: r, green: g, blue: b, alpha: a)
                    return
                }
            }
        }
        
        return nil
    }
    
    convenience init?(rgbString: String) {
        let components = rgbString
            .replacingOccurrences(of: "rgba(", with: "")
            .replacingOccurrences(of: "rgb(", with: "")
            .replacingOccurrences(of: ")", with: "")
            .split(separator: ",")
            .compactMap { CGFloat(Double($0.trimmingCharacters(in: .whitespaces)) ?? -1) }
        
        if components.count == 3 || components.count == 4 {
            self.init(
                red: components[0] / 255,
                green: components[1] / 255,
                blue: components[2] / 255,
                alpha: components.count == 4 ? components[3] : 1.0
            )
            return
        }
        
        return nil
    }
}

class SetColorArgs: Decodable {
    let input: String
}


class SafeareasPlugin: Plugin {
    override func load(webview: WKWebView) {
        
    }
    
    @objc public func setColor(_ invoke: Invoke) throws {
        
        let args = try invoke.parseArgs(SetColorArgs.self)
        
        guard let color = UIColor(colorString: args.input) else {
            invoke.reject("Invalid color format.")
            return
        }
        
        DispatchQueue.main.async {
            if let ws = UIApplication.shared.connectedScenes.first as? UIWindowScene {
                if let window = ws.windows.first {
                    // Status bar color
                    if let existingStatusBar = window.viewWithTag(MAGIC_NUMBER) {
                        existingStatusBar.backgroundColor = color
                    } else {
                        if let statusBarManager = ws.statusBarManager {
                            let statusBar = UIView(frame: statusBarManager.statusBarFrame.insetBy(dx: 0, dy: -8))
                            statusBar.backgroundColor = color
                            statusBar.tag = MAGIC_NUMBER
                            window.addSubview(statusBar)
                        }
                    }
                    
                    // Navigation bar (toolbar) color
                    UINavigationBar.appearance().barTintColor = color
                    UINavigationBar.appearance().tintColor = .white // Optional: set icon/text color to white
                    UINavigationBar.appearance().titleTextAttributes = [
                        .foregroundColor: UIColor.white
                    ]
                    
                    // Set color for the safe areas (top and bottom)
                    let safeAreaInsets = window.safeAreaInsets
                    let topSafeArea = UIView(frame: CGRect(x: 0, y: 0, width: window.frame.width, height: safeAreaInsets.top))
                    let bottomSafeArea = UIView(frame: CGRect(x: 0, y: window.frame.height - safeAreaInsets.bottom, width: window.frame.width, height: safeAreaInsets.bottom))
                    
                    topSafeArea.backgroundColor = color
                    bottomSafeArea.backgroundColor = color
                    
                    // Add views to window for top and bottom safe areas
                    window.addSubview(topSafeArea)
                    window.addSubview(bottomSafeArea)
                    
                    // Optionally, apply color to the left and right safe areas (if needed)
                    let leftSafeArea = UIView(frame: CGRect(x: 0, y: safeAreaInsets.top, width: safeAreaInsets.left, height: window.frame.height - safeAreaInsets.top - safeAreaInsets.bottom))
                    let rightSafeArea = UIView(frame: CGRect(x: window.frame.width - safeAreaInsets.right, y: safeAreaInsets.top, width: safeAreaInsets.right, height: window.frame.height - safeAreaInsets.top - safeAreaInsets.bottom))
                    
                    leftSafeArea.backgroundColor = color
                    rightSafeArea.backgroundColor = color
                    
                    // Add views to window for left and right safe areas
                    window.addSubview(leftSafeArea)
                    window.addSubview(rightSafeArea)
                }
            }
        }
        
        invoke.resolve()
    }

    @objc public func setTopBarColor(_ invoke: Invoke) throws {
    let args = try invoke.parseArgs(SetColorArgs.self)
    
    guard let color = UIColor(colorString: args.input) else {
        invoke.reject("Invalid color format.")
        return
    }
    
    DispatchQueue.main.async {
        if let ws = UIApplication.shared.connectedScenes.first as? UIWindowScene,
           let window = ws.windows.first {
            
            let topBarTag = MAGIC_NUMBER
            
            // Remove any existing status bar view
            if let existingStatusBar = window.viewWithTag(topBarTag) {
                existingStatusBar.removeFromSuperview()
            }
            
            // Add a new status bar view
            let topSafeArea = window.safeAreaInsets.top
            let statusBar = UIView(frame: CGRect(x: 0, y: 0, width: window.frame.width, height: topSafeArea))
            statusBar.backgroundColor = color
            statusBar.tag = topBarTag
            
            window.addSubview(statusBar)
            window.bringSubviewToFront(statusBar)
        }
    }
    
    invoke.resolve()
}

    
    @objc public func setBottomBarColor(_ invoke: Invoke) throws {
        let args = try invoke.parseArgs(SetColorArgs.self)
        
        guard let color = UIColor(colorString: args.input) else {
            invoke.reject("Invalid color format.")
            return
        }
        
        DispatchQueue.main.async {
            if let ws = UIApplication.shared.connectedScenes.first as? UIWindowScene,
               let window = ws.windows.first {
                let safeAreaInsets = window.safeAreaInsets
                let bottomSafeAreaTag = MAGIC_NUMBER + 1
                
                if let existingBottomBar = window.viewWithTag(bottomSafeAreaTag) {
                    existingBottomBar.backgroundColor = color
                } else {
                    let bottomSafeArea = UIView(frame: CGRect(x: 0, y: window.frame.height - safeAreaInsets.bottom, width: window.frame.width, height: safeAreaInsets.bottom))
                    bottomSafeArea.backgroundColor = color
                    bottomSafeArea.tag = bottomSafeAreaTag
                    window.addSubview(bottomSafeArea)
                }
            }
        }
        
        invoke.resolve()
    }

    @objc public func enableFullscreenScrolling(_ invoke: Invoke) throws {
        DispatchQueue.main.async {
            if let ws = UIApplication.shared.connectedScenes.first as? UIWindowScene,
               let window = ws.windows.first {
                window.rootViewController?.view.insetsLayoutMarginsFromSafeArea = false
                window.rootViewController?.view.frame = window.bounds
            }
        }
        invoke.resolve()
    }
}


/*private extension UIColor {
    func isLightColor() -> Bool {
        guard let components = self.cgColor.components, components.count >= 3 else { return false }
        let brightness = (components[0] * 299 + components[1] * 587 + components[2] * 114) / 1000
        return brightness > 0.5
    }
}*/

@_cdecl("init_plugin_safeareas")
func initPlugin() -> Plugin {
    return SafeareasPlugin()
}