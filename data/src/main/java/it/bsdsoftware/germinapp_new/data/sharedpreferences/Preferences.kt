package it.bsdsoftware.germinapp_new.data.sharedpreferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import it.bsdsoftware.germinapp_new.domain.entities.User


class Preferences(val context: Context) {
    fun clear() {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(name, MODE_PRIVATE)
        val myEdit: SharedPreferences.Editor = sharedPreferences.edit()
        myEdit.clear()
        myEdit.apply()
    }

    private val name = "GerminaSharedPref"

    var user: User
        get() {
            val sh: SharedPreferences = context.getSharedPreferences(name, MODE_PRIVATE)
            val id = sh.getInt("id", 0)
            val username = sh.getString("username", "")!!
            val name = sh.getString("name", "")!!
            val lastName = sh.getString("lastName", "")!!
            val token = sh.getString("token", "")!!
            return User(id, username, name, lastName, token)
        }
        set(value) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(name, MODE_PRIVATE)
            val myEdit: SharedPreferences.Editor = sharedPreferences.edit()

            myEdit.putInt("id",value.id)
            myEdit.putString("username",value.username)
            myEdit.putString("name", value.name)
            myEdit.putString("lastName", value.lastName)
            myEdit.putString("token", value.token)

            myEdit.apply()
        }
}