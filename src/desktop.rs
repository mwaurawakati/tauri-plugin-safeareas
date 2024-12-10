use serde::de::DeserializeOwned;
use tauri::{plugin::PluginApi, AppHandle, Runtime};

use crate::models::*;

pub fn init<R: Runtime, C: DeserializeOwned>(
  app: &AppHandle<R>,
  _api: PluginApi<R, C>,
) -> crate::Result<Safeareas<R>> {
  Ok(Safeareas(app.clone()))
}

/// Access to the safeareas APIs.
pub struct Safeareas<R: Runtime>(AppHandle<R>);

impl<R: Runtime> Safeareas<R> {
  pub fn set_color(&self, _payload: SetColorRequest) -> crate::Result<()> {
    Ok(())
  }

  pub fn set_top_bar_color(&self, _payload: SetColorRequest) -> crate::Result<()> {
    Ok(())
  }

  pub fn set_bottom_bar_color(&self, _payload: SetColorRequest) -> crate::Result<()> {
    Ok(())
  }
}
