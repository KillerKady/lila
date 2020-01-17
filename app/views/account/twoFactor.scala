package views.html
package account

import lila.api.Context
import lila.app.templating.Environment._
import lila.app.ui.ScalatagsTemplate._

import controllers.routes

object twoFactor {

  private val qrCode = raw(
    """<div style="width: 276px; height: 276px; padding: 10px; background: white; margin: 2em auto;"><div id="qrcode" style="width: 256px; height: 256px;"></div></div>"""
  )

  def setup(u: lila.user.User, form: play.api.data.Form[_])(implicit ctx: Context) =
    account.layout(
      title = s"${u.username} - Two-factor authentication",
      active = "twofactor",
      evenMoreJs = frag(
        jsAt("javascripts/vendor/qrcode.min.js"),
        jsTag("twofactor.form.js")
      )
    ) {
      div(cls := "account twofactor box box-pad")(
        h1("Setup two-factor authentication"),
        standardFlash(),
        postForm(cls := "form3", action := routes.Account.setupTwoFactor)(
          div(cls := "form-group")(
            "Two-factor authentication adds another layer of security to your account."
          ),
          div(cls := "form-group")(
            raw(
              """Get an app for two-factor authentication, for example Google Authenticator for <a class="blue" href="https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2">Android</a> or <a class="blue" href="https://itunes.apple.com/app/google-authenticator/id388497605?mt=8">iOS.</a>"""
            )
          ),
          div(cls := "form-group")("Scan the QR code with the app."),
          qrCode,
          div(cls := "form-group explanation")(
            "Enter your password and the authentication code generated by the app to complete the setup. You will need an authentication code every time you log in."
          ),
          form3.hidden(form("secret")),
          form3.passwordModified(form("passwd"), trans.password())(autofocus),
          form3.group(form("token"), raw("Authentication code"))(
            form3.input(_)(pattern := "[0-9]{6}", autocomplete := "off", required)
          ),
          form3.globalError(form),
          div(cls := "form-group")(
            "Note: If you lose access to your two-factor authentication codes, you can do a password reset via email."
          ),
          form3.action(form3.submit(raw("Enable two-factor authentication")))
        )
      )
    }

  def disable(u: lila.user.User, form: play.api.data.Form[_])(implicit ctx: Context) =
    account.layout(
      title = s"${u.username} - Two-factor authentication",
      active = "twofactor"
    ) {
      div(cls := "account twofactor box box-pad")(
        h1(
          i(cls := "is-green", dataIcon := "E"),
          " Two-factor authentication enabled"
        ),
        standardFlash(),
        p("Your account is protected with two-factor authentication."),
        postForm(cls := "form3", action := routes.Account.disableTwoFactor)(
          p(
            "You need your password and an authentication code from your authenticator app to disable two-factor authentication. ",
            "If you lost access to your authentication codes, you can also do a password reset via email."
          ),
          form3.password(form("passwd"), trans.password()),
          form3.group(form("token"), raw("Authentication code"))(
            form3.input(_)(pattern := "[0-9]{6}", autocomplete := "off", required)
          ),
          form3.action(form3.submit(raw("Disable two-factor authentication"), icon = None))
        )
      )
    }
}
