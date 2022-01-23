package com.example.tp_game_androidavanced

import android.R.attr
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.core.internal.deps.guava.base.Preconditions.checkArgument
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*


import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import android.R.attr.text
import android.view.KeyEvent
import androidx.test.espresso.Espresso

import androidx.test.espresso.Espresso.onData


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ExampleInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<HomePageActivity>
            = ActivityScenarioRule(HomePageActivity::class.java)

    @Test
    fun test0_HomePageToListGamePage() {
        // on recupere l'objet ayant l'id "button" et on clique dessus
        onView(withId(R.id.btHome)).perform(click())
        //verifie si la bonne page est lancée
        onView(ViewMatchers.withId(R.layout.activity_list_games))
    }

    @Test
    fun test0_HomePageTextTitle() {
        //verifie le titre de la page Home
        onView(withId(R.id.etHomeTitle)).check(matches(withText(containsString("MAP GAME"))))
    }

    @Test
    fun test0_HomePageTextButton() {
        //verifie le text du boutton de la page Home
        onView(withId(R.id.btHome)).check(matches(withText(containsString("CHERCHER UNE CARTE"))))
    }

    @Test
    fun test1_CreateMap() {
        // on recupere l'objet ayant l'id "btHome" et on clique dessus
        onView(withId(R.id.btHome)).perform(click())
        onView(ViewMatchers.withId(R.layout.activity_list_games))
        onView(withId(R.id.etMapName)).perform(typeText("espresso"), closeSoftKeyboard())
        onView(withId(R.id.btAddMap)).perform(click())
        onView(ViewMatchers.withId(R.layout.activity_add_point))
    }

    @Test
    fun test2_CreatePoint(){
        onView(withId(R.id.btHome)).perform(click())
        onView(withId(R.layout.activity_list_games))
        val listViewGames = onData(allOf(`is`(instanceOf(String::class.java)), `is`("espresso")))
        listViewGames.onChildView(withId(R.id.btEdit)).perform(click())
        onView(withId(R.layout.activity_list_points))
        onView(withId(R.id.fabAdd)).perform(click())
        onView(withId(R.layout.activity_add_point))
        onView(withId(R.id.etAddPointName)).perform(typeText("point 1"), closeSoftKeyboard())
        onView(withId(R.id.spAddType)).perform(click())
        onData(anything()).atPosition(1).perform(click())
        onView(withId(R.id.etAddLatitude)).perform(typeText("12"), closeSoftKeyboard())
        onView(withId(R.id.etAddLongitude)).perform(typeText("13"), closeSoftKeyboard())
        onView(withId(R.id.btAddPoint)).perform(click())
        onView(ViewMatchers.withId(R.layout.activity_add_point))
        onData(anything())
            .inAdapterView(withId(R.id.lvPoints))
            .atPosition(0)
            .onChildView(withId(R.id.tvLatitude))
            .check(matches(withText(startsWith("lat : 12"))))
    }

    @Test
    fun test3_EditPoint(){
        onView(withId(R.id.btHome)).perform(click())
        onView(withId(R.layout.activity_list_games))
        val listViewGames = onData(allOf(`is`(instanceOf(String::class.java)), `is`("espresso")))
        listViewGames.onChildView(withId(R.id.btEdit)).perform(click())
        onView(withId(R.layout.activity_list_points))
        onData(anything()).atPosition(0).onChildView(withId(R.id.btEdit)).perform(click())
        onView(withId(R.layout.activity_add_point))
        onView(withId(R.id.etAddLatitude))
            .perform(replaceText("15"))

        onView(withId(R.id.btAddPoint)).perform(click())
        onView(ViewMatchers.withId(R.layout.activity_add_point))
        onData(anything())
            .inAdapterView(withId(R.id.lvPoints))
            .atPosition(0)
            .onChildView(withId(R.id.tvLatitude))
            .check(matches(withText(startsWith("lat : 15"))))
    }

    @Test
    fun test4_AfficherMap(){
        onView(withId(R.id.btHome)).perform(click())
        onView(withId(R.layout.activity_list_games))
        val listViewGames = onData(allOf(`is`(instanceOf(String::class.java)), `is`("espresso")))
        listViewGames.onChildView(withId(R.id.ivMap)).perform(click())
        onView(withId(R.layout.activity_map))

    }

    @Test
    fun test5_DeletePoint(){
        onView(withId(R.id.btHome)).perform(click())
        onView(withId(R.layout.activity_list_games))
        val listViewGames = onData(allOf(`is`(instanceOf(String::class.java)), `is`("espresso")))
        //val listViewItem = onData(anything()).inAdapterView(withId(R.id.lvGame)).atPosition(0)
        listViewGames.onChildView(withId(R.id.btEdit)).perform(click())
        onView(withId(R.layout.activity_list_points))
        onData(anything()).atPosition(0).onChildView(withId(R.id.btDelete)).perform(click())
        onView(withId(R.layout.activity_list_points))
    }

    @Test
    fun test6_DeleteGame(){
        onView(withId(R.id.btHome)).perform(click())
        onView(withId(R.layout.activity_list_games))
        val listViewGames = onData(allOf(`is`(instanceOf(String::class.java)), `is`("espresso")))
        //val listViewItem = onData(anything()).inAdapterView(withId(R.id.lvGame)).atPosition(0)
        listViewGames.onChildView(withId(R.id.btDelete)).perform(click())
        onView(withId(R.layout.activity_list_games))
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("lat : 12.0")))


    }
}