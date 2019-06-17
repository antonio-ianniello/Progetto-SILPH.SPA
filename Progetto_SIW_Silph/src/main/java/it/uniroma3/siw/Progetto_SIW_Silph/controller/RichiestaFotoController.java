package it.uniroma3.siw.Progetto_SIW_Silph.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.Progetto_SIW_Silph.model.RichiestaFoto;
import it.uniroma3.siw.Progetto_SIW_Silph.service.RichiestaFotoService;
import it.uniroma3.siw.Progetto_SIW_Silph.service.RichiestaFotoValidator;

@Controller
public class RichiestaFotoController {
	@Autowired
	RichiestaFotoService richiestaFotoService;
	
	@Autowired 
	RichiestaFotoValidator richiestaFotoValidator;
	
	//utente
	@RequestMapping(value="/richiestaFoto", method=RequestMethod.POST)
	public String newRichiestaFoto(@Valid @ModelAttribute("richiestaFoto") RichiestaFoto rf,
			Model model, BindingResult bd) {
		this.richiestaFotoValidator.validate(rf, bd);
		if(!bd.hasErrors()) {
			this.richiestaFotoService.inserisci(rf);
			model.addAttribute("richiestaFoto", rf);
			return "richiestaFoto.html";
		}
		else {
			return "paginaSito.html";
		}
	}
	
	//funzionario
	@RequestMapping(value="/richiestaFoto/{id}", method= RequestMethod.GET)
	public String getRichiestaFoto(@PathVariable("id") Long id, Model model){
		if(id!=null) {
			model.addAttribute("richiestaFoto", this.richiestaFotoService.RichiestaFotoPerId(id));
			return "richiestaFoto.html";
		}
		else {
			model.addAttribute("richiesteFoto",this.richiestaFotoService.tutteLeRichiesteFoto());
			return "richiesteFoto.html";
		}
	}
		
	
	//funzionario
	@RequestMapping("/visualizzaRichieste")
	public String visualizzaRichieste(Model model){
		model.addAttribute("richiesteFoto", this.richiestaFotoService.tutteLeRichiesteFoto());
		return "richiesteFoto.html";
	}
	
	//utente
	@RequestMapping("/addRichiesta")
	public String addRichiesta(Model model) {
		model.addAttribute("richiestaFoto", new RichiestaFoto());
		return "paginaSito.html";
	}
}