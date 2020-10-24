package com.feup.cmov.acme_client.screens.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class LoginFragment : Fragment(), LoginHandler {

    private val viewModel: LoginViewModel by viewModels()
    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login, container, false)

        // Setting binding params
        binding.viewModel = viewModel
        binding.handler = this

        // Watching for login result
        viewModel.getLoginResult().observe(viewLifecycleOwner, Observer observe@ { result ->
            // This IF fixes a bug: when going back to this fragment the view model not being cleared and so Toasts are so annoying.
            if(viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED)
                return@observe

            if (result === LoginViewModel.LoginResults.INVALID_USERNAME) {
                binding.loginFragmentUsernameInput.error = "Invalid username"
            }

            if (result === LoginViewModel.LoginResults.INVALID_PASSWORD) {
                binding.loginFragmentPasswordInput.error = "Invalid password"
            }

            if (result === LoginViewModel.LoginResults.NETWORK_ERROR) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show()
            }

            if (result === LoginViewModel.LoginResults.SUCCESS) {
                Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    override fun onLoginButtonClick(v: View) {
        viewModel.performLogin()
    }

    override fun onSignupButtonClick(v: View) {
        v.findNavController()
            .navigate(R.id.action_loginFragment_to_signupFragment)
    }

}