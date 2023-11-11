package br.upf.userdept.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.upf.userdept.dto.AddressDTO;

/**
 * @author Angelo G. Capra
 */
public interface AddressRepository extends JpaRepository<AddressDTO, Long> {

	// Montando querie utilizando JPQL
	@Query("SELECT a, u FROM AddressDTO a JOIN a.user u WHERE u.id = :userId ")
	public List<AddressDTO> findByUsuario(@Param("userId") Long userId);

}
