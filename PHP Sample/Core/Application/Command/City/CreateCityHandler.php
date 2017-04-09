<?php
declare(strict_types = 1);

namespace app\Core\Application\Command\City;

use app\Core\Domain\Model\City\City;
use app\Core\Domain\Repository\City\CityRepository;

final class CreateCityHandler
{
    /**
     * @var CityRepository
     */
    private $cityRepository;

    /**
     * @param CityRepository $cityRepository
     */
    public function __construct(
        CityRepository $cityRepository
    )
    {
        $this->cityRepository = $cityRepository;
    }

    /**
     * @param CreateCityCommand $command
     */
    public function handle(CreateCityCommand $command)
    {
        $cityId = $this->cityRepository->nextIdentity();
        $city = City::create(
            $cityId,
            $command->getName(),
            $command->getCountry(),
            $command->getExtraCharge()
        );
        $this->cityRepository->add($city);
        $command->city = $city;
    }

}