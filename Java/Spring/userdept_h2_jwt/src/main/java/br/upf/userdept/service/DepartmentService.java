package br.upf.userdept.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.upf.userdept.dto.DepartmentDTO;
import br.upf.userdept.repository.DepartmentRepository;

/**
 * @author Angelo G. Capra
 */
@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;
	
	public DepartmentDTO salvar(DepartmentDTO dto) {
		return departmentRepository.save(dto);
	}
	
	public List<DepartmentDTO> listarTodos(){		
		return departmentRepository.findAll();
	}
	
	public Optional<DepartmentDTO> buscarPorId(Long id) {
		return departmentRepository.findById(id);
	}
	
	public void removerPorId(Long id) {
		departmentRepository.deleteById(id);
	}
	
}
