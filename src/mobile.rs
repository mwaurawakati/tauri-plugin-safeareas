use serde::de::DeserializeOwned;
use tauri::{
  plugin::{PluginApi, PluginHandle},
  AppHandle, Runtime,
};

use crate::models::*;

#[cfg(target_os = "ios")]
tauri::ios_plugin_binding!(init_plugin_safeareas);

// initializes the Kotlin or Swift plugin classes
pub fn init<R: Runtime, C: DeserializeOwned>(
  _app: &AppHandle<R>,
  api: PluginApi<R, C>,
) -> crate::Result<Safeareas<R>> {
  #[cfg(target_os = "android")]
  let handle = api.register_android_plugin("com.plugin.safeareas", "ExamplePlugin")?;
  #[cfg(target_os = "ios")]
  let handle = api.register_ios_plugin(init_plugin_safeareas)?;
  Ok(Safeareas(handle))
}

/// Access to the safeareas APIs.
pub struct Safeareas<R: Runtime>(PluginHandle<R>);

impl<R: Runtime> Safeareas<R> {
  pub fn set_color(&self, payload: SetColorRequest) -> crate::Result<()> {
    self
      .0
      .run_mobile_plugin("setColor", payload)
      .map_err(Into::into)
  }

  pub fn set_top_bar_color(&self, payload: SetColorRequest) -> crate::Result<()> {
    self
      .0
      .run_mobile_plugin("setTopBarColor", payload)
      .map_err(Into::into)
  }

  pub fn set_bottom_bar_color(&self, payload: SetColorRequest) -> crate::Result<()> {
    self
      .0
      .run_mobile_plugin("setBottomBarColor", payload)
      .map_err(Into::into)
  }
  
}
