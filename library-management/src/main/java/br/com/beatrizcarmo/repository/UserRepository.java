package br.com.beatrizcarmo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.beatrizcarmo.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

}
