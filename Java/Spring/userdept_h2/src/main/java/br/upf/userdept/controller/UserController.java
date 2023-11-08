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
import br.upf.userdept.dto.UserDTO;
import br.upf.userdept.service.UserService;

/**
 * @author Angelo G. Capra
 */

@RestController
@RequestMapping(value = "/userdept/user")
public class UserController {

	@Autowired // mecanismo de injeção de dependência
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping(value = "/inserir")
	@ResponseStatus(HttpStatus.CREATED)
	public UserDTO inserir(@RequestBody UserDTO user) {
		return userService.salvar(user);
	}

	@GetMapping(value = "/listarTodos")
	@ResponseStatus(HttpStatus.OK)
	public List<UserDTO> listarTodos() {
		return userService.listarTodos();
	}

	@GetMapping(value = "/buscarPorId")
	@ResponseStatus(HttpStatus.OK)
	public UserDTO buscarPorId(@RequestHeader(value = "id") Long id) {
		return userService.buscarPorId(id)
				// caso o cliente não foi encontrado...
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado."));
	}

	@DeleteMapping(value = "/delete")
	@ResponseStatus(HttpStatus.NO_CONTENT) // sem conteúdo
	public void removerUsuario(@RequestHeader(value = "id") Long id) {
		userService.buscarPorId(id) // antes de deletar, busca na base o cliente...
				.map(usuario -> { // caso encontre o usuario, remova o mesmo
					userService.removerPorId(usuario.getId());
					return Void.TYPE;
					// caso não encontre, retorna o status
				}).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado."));
	}

	/**
	 * Para implementar o atualizar, é necessário incluir o 
	 * método bean modelMapper() na classe UserdeptApplication.java  
	 * @param user
	 * @param id
	 */
	@PutMapping(value = "/atualizar")
	@ResponseStatus(HttpStatus.NO_CONTENT) // sem conteúdo
	public void atualizar(@RequestBody UserDTO user) {
		userService.buscarPorId(user.getId())
		.map(usuarioBase -> {
			//recurso do modelMap que ferifica o que esta no parâmetro para
			//atualizar na base.
			//esse recurso necessita incluir a dependência modelMap no pom.xml
			modelMapper.map(user, usuarioBase);
			userService.salvar(usuarioBase);
			return Void.TYPE;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado."));
	}
	
	@GetMapping(value = "/buscarPorEmail")
	@ResponseStatus(HttpStatus.OK)
	public UserDTO findByEmail(@RequestHeader(value = "email") String email) {
		return userService.buscarPorEmail(email);
	}
	
	@GetMapping(value = "/buscarPorParteNome")
	@ResponseStatus(HttpStatus.OK)
	public List<UserDTO> buscarPorParteNome(@RequestHeader(value = "nome") String nome) {		
		return userService.buscarPorParteNome(nome);
	}
	
	@GetMapping(value = "/buscarPorDeptoId")
	@ResponseStatus(HttpStatus.OK)
	public List<UserDTO> buscarPorDeptoId(@RequestHeader(value = "deptoId") Long deptoId) {		
		return userService.buscarPorDeptoId(deptoId);
	}
	
	
	@GetMapping(value = "/buscarPorSenha")
	@ResponseStatus(HttpStatus.OK)
	public List<UserDTO> buscarPorSenha(
			@RequestHeader(value = "senha") String senha) {
		return userService.buscarPorSenha(senha);
	}

}
