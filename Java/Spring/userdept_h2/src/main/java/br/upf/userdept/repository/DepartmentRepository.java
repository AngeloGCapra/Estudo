package br.upf.userdept.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.upf.userdept.dto.DepartmentDTO;

/**
 * @author Angelo G. Capra
 */

public interface DepartmentRepository extends JpaRepository<DepartmentDTO, Long>{

}


