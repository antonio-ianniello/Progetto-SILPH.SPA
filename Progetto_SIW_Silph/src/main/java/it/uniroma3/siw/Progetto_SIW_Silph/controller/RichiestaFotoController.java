package it.uniroma3.siw.Progetto_SIW_Silph.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.Progetto_SIW_Silph.model.Fotografia;
import it.uniroma3.siw.Progetto_SIW_Silph.model.RichiestaFoto;
import it.uniroma3.siw.Progetto_SIW_Silph.service.FotografiaService;
import it.uniroma3.siw.Progetto_SIW_Silph.service.RichiestaFotoService;
import it.uniroma3.siw.Progetto_SIW_Silph.service.RichiestaFotoValidator;

@Controller
public class RichiestaFotoController {
	@Autowired
	RichiestaFotoService richiestaFotoService;

	@Autowired
	FotografiaService fotografiaService;

	@Autowired 
	RichiestaFotoValidator richiestaFotoValidator;

	List<Long> fotoRichieste;


	@RequestMapping(value="/richiestaFoto", method=RequestMethod.POST)
	public String newRichiestaFoto(@Valid @ModelAttribute("richiestaFoto") RichiestaFoto rf,
			Model model, BindingResult bd) {
		//inizio stampe per la verifica su console del coretto funzionamento
		for(Long f: this.fotoRichieste) {
			System.out.println(f);
		}
		//fine stampa
		this.richiestaFotoValidator.validate(rf, bd);
		if(!bd.hasErrors()) {
			if(fotoRichieste!=null) {
				for(Long nomeFotografia:fotoRichieste) {
					Fotografia f= this.fotografiaService.FotografiaPerId(nomeFotografia);
					rf.addFotografia(f);
				}
			}
			this.richiestaFotoService.inserisci(rf);
			model.addAttribute("richiestaFoto", rf);		
			return "richiestaFoto.html";
		}
		else {
			model.addAttribute("richiestaFoto", rf);
			return "richiestaFotoForm.html";
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
	@RequestMapping(value="/addRichiesta", method= RequestMethod.POST)
	public String addRichiesta(@RequestParam (required=false, value="fotografieScelte")List<Long> valoriFotografie,
			Model model) {

		RichiestaFoto richiestaFoto= new RichiestaFoto();
		if(valoriFotografie!=null) {
			this.fotoRichieste=valoriFotografie;
		}
		else {
			return "home.html";
		}
		model.addAttribute("richiestaFoto", richiestaFoto);
		for(Fotografia f: richiestaFoto.getFotografie()) {
			System.out.println(f.getId());
		}
		return "richiestaFotoForm.html";
	}
}
