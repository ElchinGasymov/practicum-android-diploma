package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private val binding: ActivityRootBinding by viewBinding(CreateMethod.INFLATE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationMenu =
            binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            with(binding) {
                when (destination.id) {
                    R.id.mainFragment -> {
                        bottomNavigationView.visibility = View.VISIBLE
                    }

                    R.id.favoriteFragment -> {
                        bottomNavigationView.visibility = View.VISIBLE
                    }

                    R.id.teamFragment -> {
                        bottomNavigationView.visibility = View.VISIBLE
                    }

                    else -> {
                        bottomNavigationView.visibility = View.GONE
                    }
                }
            }
        }

        // Пример использования access token для HeadHunter API
        networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)
    }

    private fun networkRequestExample(accessToken: String) {
        // ...
    }

}
