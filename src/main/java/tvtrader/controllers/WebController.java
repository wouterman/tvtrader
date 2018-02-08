package tvtrader.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tvtrader.model.Configuration;

@Controller
public class WebController {

	@Autowired
	private Configuration config;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView showForm() {
		return new ModelAndView("welcome", "Configuration", config);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView submit( @ModelAttribute("Configuration") @Validated Configuration config, BindingResult result,
			ModelMap model, final RedirectAttributes redirectAttributes) {
		
		if (result.hasErrors()) {
			return new ModelAndView("error");
		}
		

		return showForm();
	}

}
