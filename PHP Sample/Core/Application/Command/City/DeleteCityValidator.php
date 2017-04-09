<?php
declare(strict_types = 1);

namespace app\Core\Application\Command\City;

use app\Core\Infrastructure\Exception\CoreException;
use app\Core\Domain\Repository\City\CityReadRepository;
use app\Core\Domain\Repository\Counterparty\CounterpartyReadRepository;

final class DeleteCityValidator
{
    /**
     * @var CityReadRepository
     */
    private $cityReadRepository;

    /**
     * @var CounterpartyReadRepository
     */
    private $counterpartyReadRepository;

    /**
     * DeleteCityValidator constructor.
     * @param CityReadRepository $cityReadRepository
     * @param CounterpartyReadRepository $counterpartyReadRepository
     */
    public function __construct(CityReadRepository $cityReadRepository, CounterpartyReadRepository $counterpartyReadRepository)
    {
        $this->cityReadRepository = $cityReadRepository;
        $this->counterpartyReadRepository = $counterpartyReadRepository;
    }

    /**
     * @param DeleteCityCommand $command
     */
    public function validate(DeleteCityCommand $command)
    {
        $this->guardCityExistById($command->getId());
        $this->guardCounterparty($command->getId());
    }

    /**
     * @param string $commandId
     * @throws CoreException
     */
    private function guardCityExistById(string $commandId)
    {
        if (!$this->cityReadRepository->existsById($commandId)) {
            throw new CoreException('Такого города не существует.');
        }
    }

    /**
     * @param string $commandId
     * @throws CoreException
     */
    private function guardCounterparty(string $commandId)
    {
        if ($this->counterpartyReadRepository->existByCityId($commandId)) {
            throw new CoreException('Нельзя удалить. К этому городу прикреплены контрагенты.');
        }
    }

}