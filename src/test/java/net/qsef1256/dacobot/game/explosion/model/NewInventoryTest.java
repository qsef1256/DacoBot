package net.qsef1256.dacobot.game.explosion.model;

import net.qsef1256.dacobot.DacoBotTest;
import net.qsef1256.dacobot.game.explosion.domain.inventory.NewInventoryEntity;
import net.qsef1256.dacobot.game.explosion.domain.inventory.NewInventoryRepository;
import net.qsef1256.dacobot.game.explosion.domain.itemtype.ItemTypeRepository;
import net.qsef1256.dacobot.module.account.entity.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
class NewInventoryTest extends DacoBotTest {

    private final long TEST_ID = 419761037861060619L;

    @Autowired
    private NewInventoryRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemTypeRepository typeRepository;

    @Test
    void test() {
        assertDoesNotThrow(() -> {
            repository.saveAndFlush(NewInventoryEntity.builder()
                    .discordUser(userRepository.getReferenceById(TEST_ID))
                    .itemType(typeRepository.getReferenceById(1L))
                    .amount(3)
                    .build());
        });
    }

}
