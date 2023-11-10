package br.upf.userdept.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.upf.userdept.dto.UserDTO;

/**
 * @author Angelo G. Capra
 */
public interface UserRepository extends JpaRepository<UserDTO, Long> {

	public UserDTO findByEmail(String email);

	// Montando querie utilizando implementações prontas do Spring DATA
	public List<UserDTO> findByNomeContaining(String name); // Pesquisa no campo name o que contem

	// Montando querie utilizando JPQL
	@Query("SELECT u FROM UserDTO u WHERE u.senha =:senha ORDER BY u.id DESC")
	public List<UserDTO> findByPorSenha(@Param("senha") String senha);

	// Montando querie utilizando SQL Nativo
	@Query(nativeQuery = true, value = "SELECT * FROM tb_user u INNER JOIN tb_department d ON d.id = u.dpt_id "
			+ "WHERE u.dpt_id = :dptId ORDER BY u.usr_nome ASC")
	public List<UserDTO> findByPorDeptoId(@Param("dptId") Long dptId);

	// Montando querie utilizando SQL Nativo
	@Query(nativeQuery = true, value = "SELECT * FROM tb_user u INNER JOIN tb_department d ON d.id = u.dpt_id "
			+ "WHERE u.usr_email = :email AND u.usr_nome = :nome ORDER BY u.usr_nome ASC")
	public UserDTO findByPorNomeEmail(@Param("email") String email, @Param("nome") String nome);

}
