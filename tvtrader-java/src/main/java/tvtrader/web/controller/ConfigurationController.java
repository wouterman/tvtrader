package tvtrader.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import tvtrader.model.Account;
import tvtrader.model.Configuration;
import tvtrader.model.MailConfiguration;
import tvtrader.services.ConfigurationService;
import tvtrader.services.DaoService;
import tvtrader.web.model.AccountForm;
import tvtrader.web.model.ConfigurationForm;
import tvtrader.web.model.MailConfigurationForm;
import tvtrader.web.service.MapperService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ConfigurationController {
	private static final String CONFIGURATION_VIEW = "configuration";
	private MapperService mapper;
	private ConfigurationService configService;
	private DaoService daoService;

    @Autowired
    public ConfigurationController(MapperService mapper, ConfigurationService configService, DaoService daoService) {
    	this.mapper = mapper;
    	this.configService = configService;
    	this.daoService = daoService;
    }

	@GetMapping(value = "/configuration")
	public ModelAndView showConfiguration() {
		ModelAndView model = new ModelAndView(CONFIGURATION_VIEW);

		Configuration  configuration = daoService.getConfiguration("DEFAULT").orElse(new Configuration());
		MailConfiguration mailConfiguration = daoService.getMailConfiguration("DEFAULT").orElse(new MailConfiguration());
		List<Account> accounts = daoService.getAccounts();

		if (accounts.isEmpty()) {
			accounts.add(new Account());
		}

		List<AccountForm> accountForms = new ArrayList<>();
		for (Account account : accounts) {
			accountForms.add(mapper.map(account));
		}

		ConfigurationForm configurationForm = mapper.map(configuration);
		MailConfigurationForm mailConfigurationForm = mapper.map(mailConfiguration);

		model.addObject(configurationForm);
		model.addObject(mailConfigurationForm);
		model.addObject(accountForms);

		return model;
	}

	@PostMapping(value = "/configuration")
	public String submit(@Valid ConfigurationForm configurationForm, @Valid MailConfigurationForm mailConfigurationForm, BindingResult result) {
		if (result.hasErrors()) {
			return CONFIGURATION_VIEW;
		}

		Configuration configuration = mapper.map(configurationForm);
		MailConfiguration mailConfiguration = mapper.map(mailConfigurationForm);

		daoService.save(configuration);
		configService.setConfiguration(configuration);

		daoService.save(mailConfiguration);
		configService.setMailConfiguration(mailConfiguration);

		return CONFIGURATION_VIEW;
	}

}
