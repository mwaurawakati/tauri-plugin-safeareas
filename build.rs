const COMMANDS: &[&str] = &["set_color", "set_top_bar_color", "set_bottom_bar_color"];

fn main() {
  tauri_plugin::Builder::new(COMMANDS)
    .android_path("android")
    .ios_path("ios")
    .build();
}
