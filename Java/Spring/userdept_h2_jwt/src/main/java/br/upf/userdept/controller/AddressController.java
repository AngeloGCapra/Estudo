package br.upf.userdept.controller;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import br.upf.userdept.dto.AddressDTO;
import br.upf.userdept.service.AddressService;
import br.upf.userdept.utils.TokenJWT;

/**
 * @author Angelo G. Capra
 */
@RestController
@RequestMapping(value = "/userdept/address")
public class AddressController {
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping(value = "/listarTodos")
	@ResponseStatus(HttpStatus.OK)
	public List<AddressDTO> listarTodos(@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return addressService.listarTodos();
	}
	
	@GetMapping(value = "/buscarPorUsuario")
	@ResponseStatus(HttpStatus.OK)
	public List<AddressDTO> buscarPorUsuario(@RequestHeader(value = "userId") Long userId, @RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return addressService.buscarPorUsuario(userId);
	}
	
	@PostMapping(value = "/inserir")
	@ResponseStatus(HttpStatus.CREATED)
	public AddressDTO inserir(@RequestBody AddressDTO dto, @RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return addressService.salvar(dto);
	}
	
	@GetMapping(value = "/buscarPorId")
	@ResponseStatus(HttpStatus.OK)
	public AddressDTO buscarPorId(@RequestHeader(value = "id") Long id, @RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return addressService.buscarPorId(id)
				// caso o cliente não foi encontrado...
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.BAD_REQUEST, "Endereço não encontrado."));
	}
	
	@DeleteMapping(value = "/delete")
	@ResponseStatus(HttpStatus.NO_CONTENT) // sem conteúdo
	public void removerUsuario(@RequestHeader(value = "id") Long id, @RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		addressService.buscarPorId(id) // antes de deletar, busca na base o cliente...
				.map(address -> { // caso encontre o usuario, remova o mesmo
					addressService.removerPorId(address.getId());
					return Void.TYPE;
					// caso não encontre, retorna o status
				}).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Endereço não encontrado."));
	}
	
	@PutMapping(value = "/atualizar")
	@ResponseStatus(HttpStatus.NO_CONTENT) 
	public void atualizar(@RequestBody AddressDTO addressDTO, @RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		addressService.buscarPorId(addressDTO.getId()).map(addressBase -> {
			modelMapper.map(addressDTO, addressBase);
			addressService.salvar(addressBase);
			return Void.TYPE;
		}).orElseThrow(() -> new ResponseStatusException(
				HttpStatus.BAD_REQUEST, "Endereço não encontrado."));
	}

}
