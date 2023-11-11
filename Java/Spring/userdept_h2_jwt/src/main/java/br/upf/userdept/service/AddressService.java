package br.upf.userdept.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.upf.userdept.dto.AddressDTO;
import br.upf.userdept.repository.AddressRepository;

/**
 * @author Angelo G. Capra
 */
@Service
public class AddressService {
	
	@Autowired
	private AddressRepository addressRepository;
	
	public List<AddressDTO> listarTodos(){		
		return addressRepository.findAll();
	}
	
	public List<AddressDTO> buscarPorUsuario(Long userId){		
		return addressRepository.findByUsuario(userId);
	}
	
	public AddressDTO salvar(AddressDTO dto) {
		return addressRepository.save(dto);
	}
	
	public Optional<AddressDTO> buscarPorId(Long id) {
		return addressRepository.findById(id);
	}
	
	public void removerPorId(Long id) {
		addressRepository.deleteById(id);
	}

}
