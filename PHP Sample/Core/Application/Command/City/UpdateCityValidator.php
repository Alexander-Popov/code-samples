<?php
declare(strict_types = 1);

namespace app\Core\Application\Command\City;

use app\Core\Infrastructure\Exception\CoreException;
use app\Core\Domain\Repository\City\CityReadRepository;

final class UpdateCityValidator
{
    /**
     * @var CityReadRepository
     */
    private $cityReadRepository;

    /**
     * @param CityReadRepository $cityReadRepository
     */
    public function __construct(CityReadRepository $cityReadRepository)
    {
        $this->cityReadRepository = $cityReadRepository;
    }

    /**
     * @param UpdateCityCommand $command
     */
    public function validate(UpdateCityCommand $command)
    {
        $this->guardCityExist($command->getId());
        $this->guardNameIsUniqueInCountry($command->getName(), $command->getCountry(), $command->getId());
    }

    /**
     * @param string $commandId
     * @throws CoreException
     */
    private function guardCityExist(string $commandId)
    {
        if (!$this->cityReadRepository->existsById($commandId)) {
            throw new CoreException('Такого города не существует.');
        }
    }

    /**
     * @param string $name
     * @param int $country
     * @param string $commandId
     * @throws CoreException
     */
    private function guardNameIsUniqueInCountry(string $name, string $country, string $commandId)
    {
        $city = $this->cityReadRepository->findByNameAndCountry($name, $country);
        if ($city != null && $commandId !== $city->getId()) {
            throw new CoreException('Это название города уже используется для этой страны.');
        }
    }

}