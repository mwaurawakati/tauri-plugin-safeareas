use tauri::{
  plugin::{Builder, TauriPlugin},
  Manager, Runtime,
};

pub use models::*;

#[cfg(desktop)]
mod desktop;
#[cfg(mobile)]
mod mobile;

mod commands;
mod error;
mod models;

pub use error::{Error, Result};

#[cfg(desktop)]
use desktop::Safeareas;
#[cfg(mobile)]
use mobile::Safeareas;

/// Extensions to [`tauri::App`], [`tauri::AppHandle`] and [`tauri::Window`] to access the safeareas APIs.
pub trait SafeareasExt<R: Runtime> {
  fn safeareas(&self) -> &Safeareas<R>;
}

impl<R: Runtime, T: Manager<R>> crate::SafeareasExt<R> for T {
  fn safeareas(&self) -> &Safeareas<R> {
    self.state::<Safeareas<R>>().inner()
  }
}

/// Initializes the plugin.
pub fn init<R: Runtime>() -> TauriPlugin<R> {
  Builder::new("safeareas")
    .invoke_handler(tauri::generate_handler![commands::set_color, commands::set_top_bar_color, commands::set_bottom_bar_color, commands::enable_fullscreen_scrolling])
    .setup(|app, api| {
      #[cfg(mobile)]
      let safeareas = mobile::init(app, api)?;
      #[cfg(desktop)]
      let safeareas = desktop::init(app, api)?;
      app.manage(safeareas);
      Ok(())
    })
    .build()
}
