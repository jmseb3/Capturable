import AppKit

final class ShareApp: NSObject, NSSharingServicePickerDelegate {
    private var window: NSWindow?
    private var picker: NSSharingServicePicker?

    func run(items: [Any], x: Double?, y: Double?) {
        let app = NSApplication.shared
        app.setActivationPolicy(.regular)
        app.activate(ignoringOtherApps: true)

        let point: NSPoint
        if let x = x, let y = y {
            point = NSPoint(x: x, y: y)
        } else {
            point = NSEvent.mouseLocation
        }

        let rect = NSRect(
            x: point.x,
            y: point.y,
            width: 1,
            height: 1
        )

        let window = NSWindow(
            contentRect: rect,
            styleMask: [.borderless],
            backing: .buffered,
            defer: false
        )

        window.level = .floating
        window.backgroundColor = .clear
        window.isOpaque = false
        window.hasShadow = false
        window.ignoresMouseEvents = false
        window.makeKeyAndOrderFront(nil)

        self.window = window

        guard let contentView = window.contentView else {
            app.terminate(nil)
            return
        }

        let picker = NSSharingServicePicker(items: items)
        picker.delegate = self
        self.picker = picker

        picker.show(
            relativeTo: contentView.bounds,
            of: contentView,
            preferredEdge: .minY
        )
    }

    func sharingServicePicker(
        _ sharingServicePicker: NSSharingServicePicker,
        didChoose service: NSSharingService?
    ) {
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            NSApplication.shared.terminate(nil)
        }
    }
}

func argValue(_ name: String) -> String? {
    let args = Array(CommandLine.arguments)
    guard let index = args.firstIndex(of: name),
          index + 1 < args.count else {
        return nil
    }
    return args[index + 1]
}

let args = CommandLine.arguments

guard args.count >= 3 else {
    print("""
          Usage:
            ./mac-share --text "hello"
            ./mac-share --url "https://example.com"
            ./mac-share --file "/path/to/file.pdf"
          """)
    exit(1)
}

let mode = args[1]
let value = args[2]

let x = argValue("--x").flatMap { Double($0) }
let y = argValue("--y").flatMap { Double($0) }

let item: Any

switch mode {
case "--text":
    item = value as NSString

case "--url":
    guard let url = URL(string: value) else {
        print("Invalid URL")
        exit(1)
    }
    item = url as NSURL

case "--file":
    item = NSURL(fileURLWithPath: value)

default:
    print("Unknown mode: \(mode)")
    exit(1)
}

let appDelegate = ShareApp()

DispatchQueue.main.async {
    appDelegate.run(items: [item], x: x, y: y)
}

NSApplication.shared.run()