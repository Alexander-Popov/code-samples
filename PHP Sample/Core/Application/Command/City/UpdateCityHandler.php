<?php
declare(strict_types = 1);

namespace app\Core\Application\Command\City;

use app\Core\Domain\Repository\City\CityReadRepository;
use app\Core\Domain\Repository\City\CityRepository;

final class UpdateCityHandler
{
    /**
     * @var CityRepository
     */
    private $cityRepository;

    /**
     * @var CityReadRepository
     */
    private $cityReadRepository;

    /**
     * @param CityRepository $cityRepository
     * @param CityReadRepository $cityReadRepository
     */
    public function __construct(
        CityRepository $cityRepository,
        CityReadRepository $cityReadRepository
    )
    {
        $this->cityRepository = $cityRepository;
        $this->cityReadRepository = $cityReadRepository;
    }

    /**
     * @param UpdateCityCommand $command
     */
    public function handle(UpdateCityCommand $command)
    {
        $city = $this->cityReadRepository->findById($command->getId());
        $city->changePlace(
            $command->getName(),
            $command->getCountry()
        );
        $city->changeExtraCharge($command->getExtraCharge());
        $this->cityRepository->save($city);
    }
}