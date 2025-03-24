package es.codeurjc.daw.alphagym.service;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

import es.codeurjc.daw.alphagym.dto.NutritionDTO;
import es.codeurjc.daw.alphagym.dto.NutritionMapper;

import javax.sql.rowset.serial.SerialBlob;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.codeurjc.daw.alphagym.model.Nutrition;
import es.codeurjc.daw.alphagym.model.NutritionComment;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.NutritionCommentRepository;
import es.codeurjc.daw.alphagym.repository.NutritionRepository;
import es.codeurjc.daw.alphagym.repository.UserRepository;


@Service
public class NutritionService {

    @Autowired
    private NutritionRepository nutritionRepository;
    @Autowired
    private NutritionCommentRepository nutritionCommentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NutritionMapper nutritionMapper;


    public Nutrition createNutrition(Nutrition nutrition, User user) throws SQLException, IOException {
        Nutrition newNutrition = new Nutrition(nutrition.getName(),nutrition.getCalories(), nutrition.getGoal(), nutrition.getDescription());
        newNutrition.setUser(user);
        if (nutrition.getImgNutrition() != null) {
            newNutrition.setImgNutrition(nutrition.getImgNutrition());
        } else {
            ClassPathResource imgFileDefault = new ClassPathResource("static/images/emptyImage.png");
            byte[] imageBytesDefault = Files.readAllBytes(imgFileDefault.getFile().toPath());
            Blob imageBlobDefault = new SerialBlob(imageBytesDefault);
            newNutrition.setImgNutrition(imageBlobDefault);
        }
        nutritionRepository.save(newNutrition);
        return newNutrition;
    }

    public List<Nutrition> getAllNutritions(){
        List<Nutrition> listNutrition = nutritionRepository.findAll();
        return listNutrition.isEmpty() ? null : listNutrition;
    }
    
    public Nutrition getNutrition (Long id){
        Optional<Nutrition> theNutrition = nutritionRepository.findById(id);
        if (theNutrition.isPresent()){
            return theNutrition.get();
        } else {
            return null;
        }
    }

    public Nutrition editDiet(Long nutritionId, Nutrition nutrition , User user){
        Optional<Nutrition> existingNutritionOpt = nutritionRepository.findById(nutritionId);

        if (existingNutritionOpt.isPresent()) {
            Nutrition existingNutrition = existingNutritionOpt.get();

            existingNutrition.setName(nutrition.getName());
            existingNutrition.setCalories(nutrition.getCalories());
            existingNutrition.setGoal(nutrition.getGoal());
            existingNutrition.setDescription(nutrition.getDescription());

            if (nutrition.getImgNutrition() != null) {
                existingNutrition.setImgNutrition(nutrition.getImgNutrition());
                existingNutrition.setImage(true);
            }

            if (existingNutrition.getUser() != null) {
                existingNutrition.setUser(user);
            }

            return nutritionRepository.save(existingNutrition);
        }

        return null;
    }

    public Nutrition deleteDiet(Long id){
        Optional<Nutrition> theDiet = nutritionRepository.findById(id);
        if (theDiet.isPresent()) {
            Nutrition nutrition = theDiet.get();
            List<User> usersWithNutrition = userRepository.findByNutritionsContaining(nutrition);
             for (User user : usersWithNutrition) {
                 user.getNutritions().remove(nutrition);
                 userRepository.save(user); 
             }
            List<NutritionComment> nutritionComments = nutritionCommentRepository.findByNutritionId(id);

            for (NutritionComment nutritionComment : nutritionComments) {
                nutritionCommentRepository.delete(nutritionComment);

            }
            nutritionRepository.delete(nutrition);
            return nutrition;
        }
        return null;
    }

    public void subscribeNutrition(Long id , User user) {
         Optional<Nutrition> nutrition = nutritionRepository.findById(id);
         if (nutrition.isPresent()) {
             user.getNutritions().add(nutrition.get());
             userRepository.save(user);
         }
     }
 
     public void unsubscribeNutrition(Long id, User user) {
         Optional<Nutrition> nutrition = nutritionRepository.findById(id);
         if (nutrition.isPresent()) {
             user.getNutritions().remove(nutrition.get());
             userRepository.save(user);
         }
     }

     public Optional<Nutrition> findById(Long id) {
        return nutritionRepository.findById(id);
    }

    public List<Nutrition> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return nutritionRepository.findAll(); // If there is no search, return all
        }
        return nutritionRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public boolean isOwner(Long nutritionId, Authentication authentication) {
        return nutritionRepository.findWithUserById(nutritionId)
                .map(nutrition -> {
                    User user = nutrition.getUser();
                    return user != null && authentication.getName().equals(user.getEmail());
                })
                .orElse(false);
    }

    //Method to get paginated diets
    public List<Nutrition> getPaginatedDiets(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return nutritionRepository.findAll(pageable).getContent();
    }

    public Collection<NutritionDTO> getAllNutritionsDTO() {
        return toDTOs(nutritionRepository.findAll());
    }   

    public NutritionDTO getNutritionDTO(Long id) {
        return toDTO(nutritionRepository.findById(id).orElseThrow());
    }

    public NutritionDTO createNutritionDTO(NutritionDTO nutritionDTO) {
        if (nutritionDTO.id() != null) {
            throw new IllegalArgumentException();
        }

        Nutrition nutrition = toDomain(nutritionDTO);

        nutritionRepository.save(nutrition);

        //book.getShops().replaceAll(shop -> shopRepository.findById(shop.getId()).orElseThrow());

        return toDTO(nutrition);
    }

    public NutritionDTO editDietDTO(Long id, NutritionDTO updatedNutritionDTO) throws SQLException {
        
        Nutrition oldNutrition = nutritionRepository.findById(id).orElseThrow();
        Nutrition updatedNutrition = toDomain(updatedNutritionDTO);
        updatedNutrition.setId(id);

        if (oldNutrition.getImage() && updatedNutrition.getImage()){

            updatedNutrition.setImgNutrition(BlobProxy.generateProxy(oldNutrition.getImgNutrition().getBinaryStream(), 
            oldNutrition.getImgNutrition().length()));
        }

        nutritionRepository.save(updatedNutrition);

        return toDTO(updatedNutrition);
    }

    public NutritionDTO createOrReplaceNutrition(Long id, NutritionDTO nutritionDTO) throws SQLException {
        
        NutritionDTO nutrition;
        if(id == null){
                nutrition = createNutritionDTO(nutritionDTO);
        } else {
                nutrition = editDietDTO(id, nutritionDTO);
        }
        return nutrition;
    }

    public NutritionDTO deleteDietDTO(Long id) {

        Nutrition nutrition = nutritionRepository.findById(id).orElseThrow();

        NutritionDTO nutritionDTO = toDTO(nutrition);
        nutritionRepository.deleteById(id);
        return nutritionDTO;
    }

    public Resource getNutritionImage(Long id) throws SQLException {

        Nutrition nutrition = nutritionRepository.findById(id).orElseThrow();
        
        if (nutrition.getImgNutrition() != null) {
                return  new InputStreamResource(nutrition.getImgNutrition().getBinaryStream());
        } else {
                throw new NoSuchElementException();
        }
    }

    public void createNutritionImage(Long id, InputStream inputStream, long size){

        Nutrition nutrition = nutritionRepository.findById(id).orElseThrow();
        
        nutrition.setImage(true);
        nutrition.setImgNutrition(BlobProxy.generateProxy(inputStream, size));

        nutritionRepository.save(nutrition);
    }

    public void replaceNutritionImage(long id, InputStream inputStream, long size) {

		Nutrition nutrition = nutritionRepository.findById(id).orElseThrow();

		if (nutrition.getImage() == null) {
			throw new NoSuchElementException();
		}

		nutrition.setImgNutrition(BlobProxy.generateProxy(inputStream, size));

		nutritionRepository.save(nutrition);
	}

	public void deleteNutritionImage(long id) {

		Nutrition nutrition = nutritionRepository.findById(id).orElseThrow();

		if (!nutrition.getImage()) {
			throw new NoSuchElementException();
		}

		nutrition.setImgNutrition(null);
		nutrition.setImage(false);

		nutritionRepository.save(nutrition);
	}

    public NutritionDTO toDTO(Nutrition nutrition) {
		return nutritionMapper.toDTO(nutrition);
	}

    public Nutrition toDomain(NutritionDTO nutritionDTO) {
		return nutritionMapper.toDomain(nutritionDTO);
	}

	public Collection<NutritionDTO> toDTOs(Collection<Nutrition> nutritions) {
		return nutritionMapper.toDTOs(nutritions);
	}

    public List<NutritionDTO> getPaginatedNutritionsDTO(Long id, int page, int limit) {
        return nutritionRepository
                .findById(id, PageRequest.of(page, limit))
                .map(nutritionMapper::toDTO)
                .toList();
    }
}


