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
	public DepartmentDTO inserir(@RequestBody DepartmentDTO departmentDTO) {
		return departmentService.salvar(departmentDTO);
	}

	@GetMapping(value = "/listarTodos")
	@ResponseStatus(HttpStatus.OK)
	public List<DepartmentDTO> listarTodos() {
		return departmentService.listarTodos();
	}

	@GetMapping(value = "/buscarPorId")
	@ResponseStatus(HttpStatus.OK)
	public DepartmentDTO buscarPorId(@RequestHeader(value = "id") Long id) {
		return departmentService.buscarPorId(id)
				// caso o cliente não foi encontrado...
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Departmento não encontrado."));
	}

	@DeleteMapping(value = "/delete")
	@ResponseStatus(HttpStatus.NO_CONTENT) // sem conteúdo
	public void removerUsuario(@RequestHeader(value = "id") Long id) {
		departmentService.buscarPorId(id) // antes de deletar, busca na base o cliente...
				.map(department -> { // definindo a variável no map
					departmentService.removerPorId(department.getId()); // caso encontre o usuario, remova o mesmo
					return Void.TYPE;
					// caso não encontre, retorna o status
				}).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado."));
	}

	/**
	 * Para implementar o atualizar, é necessário incluir o 
	 * método bean modelMapper() na classe UserdeptApplication.java 
	 * @param departmentDTO
	 * @param id
	 */
	@PutMapping(value = "/atualizar")
	@ResponseStatus(HttpStatus.NO_CONTENT) // sem conteúdo
	public void atualizar(@RequestBody DepartmentDTO departmentDTO) {
		departmentService.buscarPorId(departmentDTO.getId()).map(departmentBase -> {// definindo a variável no map
			//recurso do modelMap que ferifica o que esta no parâmetro para
			//atualizar na base.
			//esse recurso necessita de incluir a dependência modelMap no pom.xml
			modelMapper.map(departmentDTO, departmentBase); 
			departmentService.salvar(departmentBase); // salvar os itens alterados
			return Void.TYPE;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado."));
	}
}
