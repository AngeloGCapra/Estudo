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
import br.upf.userdept.dto.DepartmentDTO;
import br.upf.userdept.service.DepartmentService;
import br.upf.userdept.utils.TokenJWT;

/**
 * @author Angelo G. Capra
 */
@RestController
@RequestMapping(value = "/userdept/department")
public class DepartmentController {
	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping(value = "/inserir")
	@ResponseStatus(HttpStatus.CREATED)
	public DepartmentDTO inserir(@RequestBody DepartmentDTO departmentDTO, @RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return departmentService.salvar(departmentDTO);
	}

	@GetMapping(value = "/listarTodos")
	@ResponseStatus(HttpStatus.OK)
	public List<DepartmentDTO> listarTodos(@RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return departmentService.listarTodos();
	}

	@GetMapping(value = "/buscarPorId")
	@ResponseStatus(HttpStatus.OK)
	public DepartmentDTO buscarPorId(@RequestHeader(value = "id") Long id, @RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		return departmentService.buscarPorId(id)
				// Caso o cliente não foi encontrado...
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, 
						"Departamento não encontrado."));
	}

	@DeleteMapping(value = "/delete")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removerUsuario(@RequestHeader(value = "id") Long id, @RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		departmentService.buscarPorId(id) // Antes de deletar, busca na base o cliente...
				.map(department -> { // Definindo a variável no map
					departmentService.removerPorId(department.getId()); // Caso encontre o usuario, remova o mesmo
					return Void.TYPE;
					// Caso não encontre, retorna o status
				}).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, 
						"Departamento não encontrado."));
	}

	/**
	 * Para implementar o atualizar, é necessário incluir o 
	 * método bean modelMapper() na classe UserdeptApplication.java 
	 * @param departmentDTO
	 * @param id
	 */
	@PutMapping(value = "/atualizar")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizar(@RequestBody DepartmentDTO departmentDTO, @RequestHeader(value = "token") String token) {
		TokenJWT.validarToken(token);
		departmentService.buscarPorId(departmentDTO.getId()).map(departmentBase -> {// Definindo a variável no map
			modelMapper.map(departmentDTO, departmentBase); 
			departmentService.salvar(departmentBase); // Salvar os itens alterados
			return Void.TYPE;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, 
				"Departamento não encontrado."));
	}
}
